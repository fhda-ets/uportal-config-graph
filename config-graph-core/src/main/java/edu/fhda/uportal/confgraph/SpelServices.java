package edu.fhda.uportal.confgraph;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

/**
 * Spring service to parse SPeL expressions, and cache the expression objects for quick re-use.
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
@Component
public class SpelServices {

    private ExpressionParser expressionParser = new SpelExpressionParser();

    /**
     * Get an expression from the cache, or parse and add to the cache if not previously seen.
     * @param source Expression to parse
     * @return SpEL expression object
     */
    @Cacheable(value = "aclSpelExpressions", sync = true)
    public Expression parseExpression(String source) {
        return expressionParser.parseExpression(source);
    }

}
