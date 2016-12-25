<#assign tradeId=xml.TradeList.SWAP[0].TradeId/>
${request.setHeader('TradeId', tradeId?replace("TradeId", ""))}
${tradeId},${xml.TradeList.SWAP[0].Assets.ASSET[0].dmAssetId},${xml.TradeList.SWAP[0].Assets.ASSET[1].dmAssetId}