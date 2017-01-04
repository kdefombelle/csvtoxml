<!-- XPath syntax -->
<#-- get xpath with a parametrized value-->
<#-- xpath = "/MxML/trades/tradeBody/bond/quantity[1]"-->
<#assign param = "quantity">
${mxml["/MxML/trades/tradeBody/bond/${param}[1]"]}

<#-- optional tradeGlobalId reference, Does not work for lists -->
<#-- an absent optional tag-->
<#if mxml["count(/MxML/events/mainEvent/object/optionaltag[1])"] == 0>
No optional tag;
<#else>
${mxml["/MxML/events/mainEvent/object/optionaltag[1]"]};
</#if>
<#-- equivalent to above, just shorter-->
<#if xml["/SWAP/Assets/ASSET/STUB_Date1[1]"]?has_content>${xml["/SWAP/Env/ENV/Cust"]}</#if>
<#-- or (alternative syntax) -->
<#if mxml["/MxML/events/mainEvent/object/objectNature[1]"]?size==0>
No optional tag;
<#else>
${mxml["/MxML/events/mainEvent/object/objectNature[1]"]};
</#if>


<!-- Scope test -->
<#assign test="this the test value"/>

<#function testFunction>
	<#assign test="the test is the test in the function"/>
	<#assign test2=test+" test2"/>
	<#return test2>
</#function>

${test}
${testFunction()}

<!-- number format test -->
<#setting locale="fr">
<#assign varString="1,2345">
<#assign var=varString?string>
${var}
<#--setting number_format="0.###E0"/>
<#setting locale="en_US">
${var?string("0.###")}
<#setting locale="hu">
${var?string("0.####")}-->

<!-- date comparaison -->
<#assign chaine1="22081978">
<#assign date1=chaine1?date("ddMMyyyy")>
<#assign chaine2="28081981">
<#assign date2=chaine2?date("ddMMyyyy")>
${date1?string.full}  
${date2?string.full}  
<#if date2<date1>TRUE
<#else>FALSE</#if>
