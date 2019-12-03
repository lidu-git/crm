$(function () {
    var lossId = $("#lossId").val();
    $("#dg").edatagrid({
        url: ctx + "/customer_repri/customerReprieveByLossId?lossId=" + lossId,
        saveUrl: ctx + "/customer_repri/insertReprive?lossId=" + lossId,
        updateUrl: ctx + "/customer_repri/updateReprive?lossId=" + lossId
    })
    var state = $("#state").val();
    if (state == 1) {
        $("#toolbar").remove();
        $("#dg").edatagrid("disableEditing");
    }
});
function saveCustomerRepri() {
    $("#dg").edatagrid("saveRow");
    $("#dg").edatagrid("load");
}
function delCustomerRepri() {
    var rows = $("#dg").edatagrid("getSelections");

    if (rows.length == 0) {
        $.messager.alert("来自crm", "请先选择要删除的记录", "info");
        return;
    }

    $.messager.confirm("crm", "确定要删除所选项?", function (r) {
        if (r) {
            $.ajax({
                type: "post",
                url: ctx + "/customer_repri/delete",
                data: "id=" + rows[0].id,
                dataType: "json",
                success: function (data) {
                    $.messager.alert("来自crm", data.msg, "info");
                    if (data.code == 200) {
                        $("#dg").edatagrid("load");
                    }
                }
            })
        }
    })
}
function updateCustomerLossState() {
    $.messager.confirm("crm", "确定该客户已流失?", function (r) {
        if (r) {
            $.messager.prompt("crm", "请输入流失原因", function (msg) {
                if (msg) {
                    $.ajax({
                        type: "post",
                        url: ctx + "/customer_loss/updateCustomerLossState",
                        data: "lossId=" + $("#lossId").val() + "&lossReason=" + msg,
                        dataType: "json",
                        success: function (data) {
                            $.messager.alert("来自crm", data.msg, "info");
                            if (data.code == 200) {
                                $("#toolbar").remove();
                            }
                        }
                    })
                }else {
                    $.messager.alert("来自crm", "流失原因不能为空!!", "info");
                }
            })

        }
    })
}