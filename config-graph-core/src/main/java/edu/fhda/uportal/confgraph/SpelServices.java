package edu.fhda.uportal.confgraph;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
@Component
public class SpelServices {

    private ExpressionParser expressionParser = new SpelExpressionParser();

    @Cacheable(value = "aclSpelExpressions", sync = true)
    public Expression handleExpression(String source) {
        return expressionParser.parseExpression(source);
    }

}
