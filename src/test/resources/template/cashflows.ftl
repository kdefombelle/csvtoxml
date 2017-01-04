summitId,leg1.summitId,leg2.summitId,cashflows
${xml["/TradeList/SWAP[1]/TradeId"]},${xml["/TradeList/SWAP[1]/Assets/ASSET[1]/dmAssetId"]},${xml["/TradeList/SWAP[1]/Assets/ASSET[2]/dmAssetId"]}
${xml.TradeList.SWAP[0].TradeId},${xml.TradeList.SWAP[0].Assets.ASSET[0].dmAssetId},${xml.TradeList.SWAP[0].Assets.ASSET[1].dmAssetId},<#list xml.TradeList.SWAP[0].Assets.ASSET[0].SCHEDULE.Row as flow>${flow.Start};${flow.End};${flow.Fixing}<#sep>|</#sep></#list>

<#list xml["/SWAP/Assets/ASSET[1]/SCHEDULE/Row"] as flow>${flow.Start};${flow.End};${flow.Fixing}<#sep>|</#sep></#list>


