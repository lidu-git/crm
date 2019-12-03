function formatterState(val) {
    if (val == 0) {
        return "暂缓流失";
    }
    if (val == 1) {
        return "确认流失";
    }
}
function formatterOp(val,rowData) {
    if(rowData.state==0){
        var href = "javascript:openCustomerRepriDetailTab('客户流失暂缓处理_"+rowData.id+"'," +rowData.id +")";
        return "<a href=" + href + ">添加暂缓措施</a>";
    }

    if(rowData.state==1){
        var href = "javascript:openCustomerRepriDetailTab('客户流失情况_"+rowData.id+"'," +rowData.id +")";
        return "<a href=" + href + ">查看流失详情</a>";
    }

}
function searchCustomerLosses() {
    $("#dg").datagrid("load",{
        customerName: $("#customerName").val(),
        customerManager :$("#customerManager").combobox("getValue"),
        customerNum:$("#customerNum").val(),
        time:$("#time").datebox("getValue")
    })
}
$(function () {

    $("#dg").datagrid({
        rowStyler: function (index, rowData) {
            if (rowData.state == 0) {
                return "background-color:yellow";

            } else if (rowData.state == 1) {
                return "background-color:red";
            }
        }
    });
});
function openCustomerRepriDetailTab(title,lossId) {
    window.parent.openTab(title,ctx+"/customer_loss/toRepreivePage/"+lossId);
}