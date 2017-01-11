${request.setHeader('TradeId', xml["/FXSWAP/TradeId/text()"])}
<@compress single_line=true>
<#--  general -->
${xml["/FXSWAP/TradeId"]},
${xml["/FXSWAP/TradeType"]},
${xml["/FXSWAP/Env/ENV/Book"]},
${xml["/FXSWAP/Env/ENV/Desk"]},
<#if xml["/FXSWAP/Env/ENV/Folder"][0]??>${xml["/FXSWAP/Env/ENV/Folder"]}</#if>,
${xml["/FXSWAP/Env/ENV/Cust"]},
${xml["/FXSWAP/Env/ENV/TradeDate"]},
<#--  FXSWAP specific -->
${xml["/FXSWAP/Forex/FOREX/PorS"]},
${xml["/FXSWAP/Forex/FOREX/BoughtCcy"]},
${xml["/FXSWAP/Forex/FOREX/BoughtAmt"]},
${xml["/FXSWAP/Forex/FOREX/SoldCcy"]},
${xml["/FXSWAP/Forex/FOREX/SoldAmt"]},
${xml["/FXSWAP/Forex/FOREX/ShortBoughtDate"]},
${xml["/FXSWAP/Forex/FOREX/ShortXRate"]},
${xml["/FXSWAP/Forex/FOREX/XRate"]},
${xml["/FXSWAP/Forex/FOREX/SpotRate"]},
${xml["/FXSWAP/Forex/FOREX/SpotDate"]},
${xml["/FXSWAP/Forex/FOREX/BoughtDate"]},
${xml["/FXSWAP/Forex/FOREX/FwdPoints"]},
${xml["/FXSWAP/Forex/FOREX/FwdBoughtAmt"]},
${xml["/FXSWAP/Forex/FOREX/FwdSoldAmt"]},
${xml["/FXSWAP/Forex/FOREX/Style"]},
${xml["/FXSWAP/Forex/FOREX/BaseRate"]},
${xml["/FXSWAP/Forex/FOREX/BaseAmount"]},
${xml["/FXSWAP/Forex/FOREX/UserSelectedFlag"]},
${xml["/FXSWAP/Forex/FOREX/InterbankRate"]},
${xml["/FXSWAP/Forex/FOREX/InterbankFwdPts"]},
${xml["/FXSWAP/Forex/FOREX/InterbankSpotRate"]}
</@compress>