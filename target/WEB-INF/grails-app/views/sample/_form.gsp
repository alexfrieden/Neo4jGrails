<%@ page import="neo4jplatform.Sample" %>



<div class="fieldcontain ${hasErrors(bean: sampleInstance, field: 'food', 'error')} ">
    <label for="food">
        <g:message code="sample.food.label" default="Food"/>
    </label>
    <g:textField name="food" value="${sampleInstance?.food}"/>
</div>

