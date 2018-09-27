package com.xiao.demo.utils;

import com.sun.istack.internal.Nullable;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Range;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static org.springframework.util.ObjectUtils.nullSafeEquals;
import static org.springframework.util.ObjectUtils.nullSafeHashCode;

/**
 * Created by xiao on 2018/8/15.
 */

public class JpaQueryUtilsTest {

    @Test
    public void testPartion() throws Exception {
        String sql = "select * from user t where id=: id and name=: userName and type in (:type123) or con like '%:connnn%' ";

        Matcher matcher = PARAMETER_BINDING_PATTERN.matcher(sql);
        int s = matcher.groupCount();
        matcher.matches();
    }

    @Test
    public void testJpa() throws Exception {
//        String sql = "select * from user t where name= :userName ";
        String sql = "select * from user t where id=:id and name=:userName and type in (:type123) or con like %:connnn% and ppp = ?5 ";
        List<ParameterBinding> list = new ArrayList<>();
        ParameterBinding p1 = new ParameterBinding("id",1,"123");
        ParameterBinding p2 = new ParameterBinding("userName",2,"123");
        ParameterBinding p3 = new ParameterBinding("type123",3,"xxxxxx");
        ParameterBinding p4 = new ParameterBinding("connnn",4,"bbbbb");
        ParameterBinding p5 = new ParameterBinding("ppp__",5,"123");
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);
        list.add(p5);
        String parser = this.parseParameterBindingsOfQueryIntoBindingsAndReturnCleanedQuery(sql,list);
        parser.toString();
    }

    private static final String IDENTIFIER = "[._[\\P{Z}&&\\P{Cc}&&\\P{Cf}&&\\P{P}]]+";
    static final String COLON_NO_DOUBLE_COLON = "(?<![:\\\\]):";
    static final String IDENTIFIER_GROUP = String.format("(%s)", IDENTIFIER);
    static final String EXPRESSION_PARAMETER_PREFIX = "__$synthetic$__";
    private static final Pattern PARAMETER_BINDING_BY_INDEX = Pattern.compile("\\?(\\d+)");
    private static Pattern PARAMETER_BINDING_PATTERN;
    private static final String MESSAGE = "Already found parameter binding with same index / parameter name but differing binding type! "
            + "Already have: %s, found %s! If you bind a parameter multiple times make sure they use the same binding.";
    public static final int INDEXED_PARAMETER_GROUP = 4;
    public static final int NAMED_PARAMETER_GROUP = 6;
    public static final int COMPARISION_TYPE_GROUP = 1;
    public static final int EXPRESSION_GROUP = 9;

    private static String tmpPart ;

    static  {
        List<String> keywords = new ArrayList<>();

        for (ParameterBindingType type : ParameterBindingType.values()) {
            if (type.getKeyword() != null) {
                keywords.add(type.getKeyword());
            }
        }

        StringBuilder builder = new StringBuilder();
        builder.append("(");
        builder.append(StringUtils.collectionToDelimitedString(keywords, "|")); // keywords
        builder.append(")?");
        builder.append("(?: )?"); // some whitespace
        builder.append("\\(?"); // optional braces around parameters
        builder.append("(");
        builder.append("%?(\\?(\\d+))%?"); // position parameter and parameter index
        builder.append("|"); // or

        // named parameter and the parameter name
        builder.append("%?(" + COLON_NO_DOUBLE_COLON + IDENTIFIER_GROUP + ")%?");
        builder.append("|"); // or
        builder.append("%?((:|\\?)#\\{([^}]+)\\})%?"); // expression parameter and expression
        builder.append(")");
        builder.append("\\)?"); // optional braces around parameters
        tmpPart = builder.toString();

        PARAMETER_BINDING_PATTERN = Pattern.compile(builder.toString(), CASE_INSENSITIVE);
    }

    String parseParameterBindingsOfQueryIntoBindingsAndReturnCleanedQuery(String query,
                                                                          List<ParameterBinding> bindings) {

        String result = query;
        Matcher matcher = PARAMETER_BINDING_PATTERN.matcher(query);

        int greatestParameterIndex = tryFindGreatestParameterIndexIn(query);

        boolean parametersShouldBeAccessedByIndex = greatestParameterIndex != -1;

			/*
			 * Prefer indexed access over named parameters if only SpEL Expression parameters are present.
			 */
        if (!parametersShouldBeAccessedByIndex && query.contains("?#{")) {
            parametersShouldBeAccessedByIndex = true;
            greatestParameterIndex = 0;
        }

			/*
			 * If parameters need to be bound by index, we bind the synthetic expression parameters starting from position of the greatest discovered index parameter in order to
			 * not mix-up with the actual parameter indices.
			 */
        int expressionParameterIndex = parametersShouldBeAccessedByIndex ? greatestParameterIndex : 0;

        QuotationMap quotationMap = new QuotationMap(query);
        String tmp = query;

        int i = 0;
        while (matcher.find()) {

            if (quotationMap.isQuoted(matcher.start())) {
                continue;
            }



            String parameterIndexString = matcher.group(INDEXED_PARAMETER_GROUP);
            String parameterName = parameterIndexString != null ? null : matcher.group(NAMED_PARAMETER_GROUP);
            Integer parameterIndex = parameterIndexString == null ? null : Integer.valueOf(parameterIndexString);
            String typeSource = matcher.group(COMPARISION_TYPE_GROUP);
            String expression = null;
            String replacement = null;

            if (parameterName == null && parameterIndex == null) {
                expressionParameterIndex++;

                if (parametersShouldBeAccessedByIndex) {
                    parameterIndex = expressionParameterIndex;
                    replacement = "?" + parameterIndex;
                } else {
                    parameterName = EXPRESSION_PARAMETER_PREFIX + expressionParameterIndex;
                    replacement = ":" + parameterName;
                }

                expression = matcher.group(EXPRESSION_GROUP);
            }

            switch (ParameterBindingType.of(typeSource)) {

                case LIKE:

                    Part.Type likeType = LikeParameterBinding.getLikeTypeFrom(matcher.group(2));
                    replacement = replacement != null ? replacement : matcher.group(3);

                    if (parameterIndex != null) {
                        checkAndRegister(new LikeParameterBinding(parameterIndex, likeType, expression), bindings);
                    } else {
                        checkAndRegister(new LikeParameterBinding(parameterName, likeType, expression), bindings);

                        replacement = expression != null ? ":" + parameterName : matcher.group(5);
                    }

                    break;

                case IN:

                    if (parameterIndex != null) {
                        checkAndRegister(new InParameterBinding(parameterIndex, expression), bindings);
                    } else {
                        checkAndRegister(new InParameterBinding(parameterName, expression), bindings);
                    }

                    break;

                case AS_IS: // fall-through we don't need a special parameter binding for the given parameter.
                default:


                    bindings.add(parameterIndex != null ? new ParameterBinding(null, parameterIndex, expression)
                            : new ParameterBinding(parameterName, null, expression));
            }

            if (replacement != null) {
                result = replaceFirst(result, matcher.group(2), replacement);
            }

            tmp = tmp.replaceFirst(tmpPart, bindings.get(i).expression);
            i ++;

        }

        return tmp;
    }

    private static String replaceFirst(String text, String substring, String replacement) {

        int index = text.indexOf(substring);
        if (index < 0) {
            return text;
        }

        return text.substring(0, index) + replacement + text.substring(index + substring.length());
    }

    private int tryFindGreatestParameterIndexIn(String query) {

        Matcher parameterIndexMatcher = PARAMETER_BINDING_BY_INDEX.matcher(query);

        int greatestParameterIndex = -1;
        while (parameterIndexMatcher.find()) {
            String parameterIndexString = parameterIndexMatcher.group(1);
            greatestParameterIndex = Math.max(greatestParameterIndex, Integer.parseInt(parameterIndexString));
        }

        return greatestParameterIndex;
    }

    private static void checkAndRegister(ParameterBinding binding, List<ParameterBinding> bindings) {

        for (ParameterBinding existing : bindings) {
            if (existing.hasName(binding.getName()) || existing.hasPosition(binding.getPosition())) {
                Assert.isTrue(existing.equals(binding), String.format(MESSAGE, existing, binding));
            }
        }

        if (!bindings.contains(binding)) {
            bindings.add(binding);
        }
    }
    
    private enum ParameterBindingType {

        // Trailing whitespace is intentional to reflect that the keywords must be used with at least one whitespace
        // character, while = does not.
        LIKE("like "), IN("in "), AS_IS(null);

        private final @Nullable
        String keyword;

        ParameterBindingType(@Nullable String keyword) {
            this.keyword = keyword;
        }

        /**
         * Returns the keyword that will tirgger the binding type or {@literal null} if the type is not triggered by a
         * keyword.
         *
         * @return the keyword
         */
        @Nullable
        public String getKeyword() {
            return keyword;
        }

        /**
         * Return the appropriate {@link StringQuery.ParameterBindingParser.ParameterBindingType} for the given {@link String}. Returns {@literal #AS_IS} in
         * case no other {@link StringQuery.ParameterBindingParser.ParameterBindingType} could be found.
         */
        static ParameterBindingType of(String typeSource) {

            if (!StringUtils.hasText(typeSource)) {
                return AS_IS;
            }

            for (ParameterBindingType type : values()) {
                if (type.name().equalsIgnoreCase(typeSource.trim())) {
                    return type;
                }
            }

            throw new IllegalArgumentException(String.format("Unsupported parameter binding type %s!", typeSource));
        }
    }

    static class ParameterBinding {

        private final @Nullable String name;
        private final @Nullable String expression;
        private final @Nullable Integer position;

        /**
         * Creates a new {@link StringQuery.ParameterBinding} for the parameter with the given position.
         *
         * @param position must not be {@literal null}.
         */
        ParameterBinding(Integer position) {
            this(null, position, null);
        }

        /**
         * Creates a new {@link StringQuery.ParameterBinding} for the parameter with the given name, position and expression
         * information. Either {@literal name} or {@literal position} must be not {@literal null}.
         *
         * @param name of the parameter may be {@literal null}.
         * @param position of the parameter may be {@literal null}.
         * @param expression the expression to apply to any value for this parameter.
         */
        ParameterBinding(@Nullable String name, @Nullable Integer position, @Nullable String expression) {

            if (name == null) {
                Assert.notNull(position, "Position must not be null!");
            }

            if (position == null) {
                Assert.notNull(name, "Name must not be null!");
            }

            this.name = name;
            this.position = position;
            this.expression = expression;
        }

        /**
         * Returns whether the binding has the given name. Will always be {@literal false} in case the
         * {@link StringQuery.ParameterBinding} has been set up from a position.
         */
        boolean hasName(@Nullable String name) {
            return this.position == null && this.name != null && this.name.equals(name);
        }

        /**
         * Returns whether the binding has the given position. Will always be {@literal false} in case the
         * {@link StringQuery.ParameterBinding} has been set up from a name.
         */
        boolean hasPosition(@Nullable Integer position) {
            return position != null && this.name == null && position.equals(this.position);
        }

        /**
         * @return the name
         */
        @Nullable
        public String getName() {
            return name;
        }

        /**
         * @return the name
         * @throws IllegalStateException if the name is not available.
         * @since 2.0
         */
        String getRequiredName() throws IllegalStateException {

            String name = getName();

            if (name != null) {
                return name;
            }

            throw new IllegalStateException(String.format("Required name for %s not available!", this));
        }

        /**
         * @return the position
         */
        @Nullable
        Integer getPosition() {
            return position;
        }

        /**
         * @return the position
         * @throws IllegalStateException if the position is not available.
         * @since 2.0
         */
        int getRequiredPosition() throws IllegalStateException {

            Integer position = getPosition();

            if (position != null) {
                return position;
            }

            throw new IllegalStateException(String.format("Required position for %s not available!", this));
        }

        /**
         * @return {@literal true} if this parameter binding is a synthetic SpEL expression.
         */
        public boolean isExpression() {
            return this.expression != null;
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {

            int result = 17;

            result += nullSafeHashCode(this.name);
            result += nullSafeHashCode(this.position);
            result += nullSafeHashCode(this.expression);

            return result;
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {

            if (!(obj instanceof ParameterBinding)) {
                return false;
            }

            ParameterBinding that = (ParameterBinding) obj;

            return nullSafeEquals(this.name, that.name) && nullSafeEquals(this.position, that.position)
                    && nullSafeEquals(this.expression, that.expression);
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return String.format("ParameterBinding [name: %s, position: %d, expression: %s]", getName(), getPosition(),
                    getExpression());
        }

        /**
         * @param valueToBind value to prepare
         */
        @Nullable
        public Object prepare(@Nullable Object valueToBind) {
            return valueToBind;
        }

        @Nullable
        public String getExpression() {
            return expression;
        }
    }

    static class InParameterBinding extends ParameterBinding {

        /**
         * Creates a new {@link StringQuery.InParameterBinding} for the parameter with the given name.
         */
        InParameterBinding(String name, @Nullable String expression) {
            super(name, null, expression);
        }

        /**
         * Creates a new {@link StringQuery.InParameterBinding} for the parameter with the given position.
         */
        InParameterBinding(int position, @Nullable String expression) {
            super(null, position, expression);
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.jpa.repository.query.StringQuery.ParameterBinding#prepare(java.lang.Object)
         */
        @Override
        public Object prepare(@Nullable Object value) {

            if (!ObjectUtils.isArray(value)) {
                return value;
            }

            int length = Array.getLength(value);
            Collection<Object> result = new ArrayList<>(length);

            for (int i = 0; i < length; i++) {
                result.add(Array.get(value, i));
            }

            return result;
        }
    }

    static class LikeParameterBinding extends ParameterBinding {

        private static final List<Part.Type> SUPPORTED_TYPES = Arrays.asList(Part.Type.CONTAINING, Part.Type.STARTING_WITH,
                Part.Type.ENDING_WITH, Part.Type.LIKE);

        private final Part.Type type;

        /**
         * Creates a new {@link StringQuery.LikeParameterBinding} for the parameter with the given name and {@link Part.Type}.
         *
         * @param name must not be {@literal null} or empty.
         * @param type must not be {@literal null}.
         */
        LikeParameterBinding(String name, Part.Type type) {
            this(name, type, null);
        }

        /**
         * Creates a new {@link StringQuery.LikeParameterBinding} for the parameter with the given name and {@link Part.Type} and parameter
         * binding input.
         *
         * @param name must not be {@literal null} or empty.
         * @param type must not be {@literal null}.
         * @param expression may be {@literal null}.
         */
        LikeParameterBinding(String name, Part.Type type, @Nullable String expression) {

            super(name, null, expression);

            Assert.hasText(name, "Name must not be null or empty!");
            Assert.notNull(type, "Type must not be null!");

            Assert.isTrue(SUPPORTED_TYPES.contains(type),
                    String.format("Type must be one of %s!", StringUtils.collectionToCommaDelimitedString(SUPPORTED_TYPES)));

            this.type = type;
        }

        /**
         * Creates a new {@link StringQuery.LikeParameterBinding} for the parameter with the given position and {@link Part.Type}.
         *
         * @param position position of the parameter in the query.
         * @param type must not be {@literal null}.
         */
        LikeParameterBinding(int position, Part.Type type) {
            this(position, type, null);
        }

        /**
         * Creates a new {@link StringQuery.LikeParameterBinding} for the parameter with the given position and {@link Part.Type}.
         *
         * @param position position of the parameter in the query.
         * @param type must not be {@literal null}.
         * @param expression may be {@literal null}.
         */
        LikeParameterBinding(int position, Part.Type type, @Nullable String expression) {

            super(null, position, expression);

            Assert.isTrue(position > 0, "Position must be greater than zero!");
            Assert.notNull(type, "Type must not be null!");

            Assert.isTrue(SUPPORTED_TYPES.contains(type),
                    String.format("Type must be one of %s!", StringUtils.collectionToCommaDelimitedString(SUPPORTED_TYPES)));

            this.type = type;
        }

        /**
         * Returns the {@link Part.Type} of the binding.
         *
         * @return the type
         */
        public Part.Type getType() {
            return type;
        }

        /**
         * Prepares the given raw keyword according to the like type.
         */
        @Nullable
        @Override
        public Object prepare(@Nullable Object value) {

            if (value == null) {
                return null;
            }

            switch (type) {
                case STARTING_WITH:
                    return String.format("%s%%", value.toString());
                case ENDING_WITH:
                    return String.format("%%%s", value.toString());
                case CONTAINING:
                    return String.format("%%%s%%", value.toString());
                case LIKE:
                default:
                    return value;
            }
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {

            if (!(obj instanceof LikeParameterBinding)) {
                return false;
            }

            LikeParameterBinding that = (LikeParameterBinding) obj;

            return super.equals(obj) && this.type.equals(that.type);
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {

            int result = super.hashCode();

            result += nullSafeHashCode(this.type);

            return result;
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return String.format("LikeBinding [name: %s, position: %d, type: %s]", getName(), getPosition(), type);
        }

        /**
         * Extracts the like {@link Part.Type} from the given JPA like expression.
         *
         * @param expression must not be {@literal null} or empty.
         */
        private static Part.Type getLikeTypeFrom(String expression) {

            Assert.hasText(expression, "Expression must not be null or empty!");

            if (expression.matches("%.*%")) {
                return Part.Type.CONTAINING;
            }

            if (expression.startsWith("%")) {
                return Part.Type.ENDING_WITH;
            }

            if (expression.endsWith("%")) {
                return Part.Type.STARTING_WITH;
            }

            return Part.Type.LIKE;
        }
    }

    static class QuotationMap {

        private static final Set<Character> QUOTING_CHARACTERS = new HashSet<>(Arrays.asList('"', '\''));

        private List<Range<Integer>> quotedRanges = new ArrayList<>();

        QuotationMap(@Nullable String query) {

            if (query == null) {
                return;
            }

            Character inQuotation = null;
            int start = 0;

            for (int i = 0; i < query.length(); i++) {

                char currentChar = query.charAt(i);

                if (QUOTING_CHARACTERS.contains(currentChar)) {

                    if (inQuotation == null) {

                        inQuotation = currentChar;
                        start = i;

                    } else if (currentChar == inQuotation) {

                        inQuotation = null;
                        quotedRanges.add(Range.of(Range.Bound.inclusive(start), Range.Bound.inclusive(i)));
                    }
                }
            }

            if (inQuotation != null) {
                throw new IllegalArgumentException(
                        String.format("The string <%s> starts a quoted range at %d, but never ends it.", query, start));
            }
        }

        /**
         * @param index to check if it is part of a quoted range.
         * @return whether the query contains a quoted range at {@literal index}.
         */
        public boolean isQuoted(int index) {
            return quotedRanges.stream().anyMatch(r -> r.contains(index));
        }
    }
}
