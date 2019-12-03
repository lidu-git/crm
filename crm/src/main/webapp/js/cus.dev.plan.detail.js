$(function () {
    var devresult = $("#devResult").val();

    if(devresult==2||devresult==3){
        $("#toolbar").remove();
    }
    $("#dg").edatagrid({
        url:ctx+"/cus_dev_plan/queryCusDevPlans?saleChanceId="+$("#saleChanceId").val(),
        saveUrl:ctx+"/cus_dev_plan/insert?saleChanceId="+$("#saleChanceId").val(),
        updateUrl:ctx+"/cus_dev_plan/update?saleChanceId="+$("#saleChanceId").val()
    })
})

function saveCusDevPlan () {
    $("#dg").edatagrid("saveRow");
    $("#dg").edatagrid("load");
}
function updateCusDevPlan() {
    $("#dg").edatagrid("saveRow");
    //$("#dg").edatagrid("load");
}
function delCusDevPlan() {
    var row=$("#dg").datagrid("getSelected");
    if(row==null){
        $.messager.alert("来自Crm","请先选中一行数据","info");
        return
    }
    $.messager.confirm("来自Crm","你确定要删除这一行数据嘛？",function (r) {
        if(r){
            $.ajax({
                type:'post',
                data:"id="+row.id,
                dataType:'json',
                url:ctx+"/cus_dev_plan/delete",
                success:function (data) {
                    if (data.code==200){
                        $.messager.alert("来自Crm",data.msg,"info");
                        $("#dg").edatagrid("load");
                    }else {
                        $.messager.alert("来自Crm",data.msg,"error");
                    }
                }
            })
        }
    })
}
function updateSaleChanceDevResult(devResult) {
    $.messager.confirm("来自crm系统","确定执行该操作",function (r) {
        if(r){
            $.ajax({
                type:"post",
                url:ctx+"/sale_chance/updateSaleChanceDevResult",
                data:"devResult="+devResult + "&saleChanceId=" + $("#saleChanceId").val(),
                dataType:"json",
                success:function (data) {
                    $.messager.alert("来自crm",data.msg,"info");
                    if(data.code==200){
                        $("#toolbar").remove();
                    }
                }
            })
        }
    })
}