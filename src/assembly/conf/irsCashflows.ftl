<#assign tradeId=xml.TradeList.SWAP[0].TradeId/>
${request.setHeader('TradeId', tradeId?replace("TradeId", ""))}
${xml.TradeList.SWAP[0].TradeId},${xml.TradeList.SWAP[0].Assets.ASSET[0].dmAssetId},${xml.TradeList.SWAP[0].Assets.ASSET[1].dmAssetId},<#list xml.TradeList.SWAP[0].Assets.ASSET[0].SCHEDULE.Row as flow>${flow.Start};${flow.End};${flow.Fixing}<#sep>|</#sep></#list>





