<#assign tradeId=xml.FRA.TradeId/>
${request.setHeader('TradeId', tradeId?replace("TradeId", ""))}
interestRateSwap,INTERFACE,MIGRATION,${xml["/FRA/Env/ENV/Cust"]},${xml["/FRA/Env/ENV/Book"]},${xml["/FRA/TradeId"]}