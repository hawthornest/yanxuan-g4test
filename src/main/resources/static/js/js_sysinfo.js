joinPage("");
function joinPage(info){
    $("#syspaginationinfo").empty();
    if (info)
    {
        lbody += "<li class=\"pagination pagination-item-active\"><a>1</a></li>";
        $("#syspaginationinfo").append(lbody);
    }
    else{
        var genXmlUrl = "/yanxuan/getInfolimit?";
        var requestParam = "limitStart=0&limitEnd=10";
        var lbody='';
        $.get(genXmlUrl+requestParam,function(resdata){
            var syscount = $.parseJSON(resdata).count;
            for(temp = 1; temp <= Math.ceil(syscount/10); temp++)
            {
                lbody += "<li class=\"pagination\"><a  id='pagesyscoutid"+temp+"'onclick=\"javascript:selectSysInfo("+(temp-1)*10+","+10+");\">"+temp+"</a></li>";
            }
            $("#syspaginationinfo").append(lbody)
        })
    }
}

selectSysInfo(0,10,'');
function selectSysInfo(limitStart,limitEnd,info) {
    $("#sysinfo").empty();
    $("#pagesyscoutid"+Math.ceil((limitStart+1)/10)).toggleClass("pagination pagination-item-active");
    var tbody = '<tr class="tablehead"><th style="font-weight:bold;font-size:20px;background:gray;text-align: center;">id</th><th style="font-weight:bold;font-size:20px;background:gray;text-align: center;" >测试服务名</th><th style="font-weight:bold;font-size:20px;background:gray;text-align: center;">收件人信息</th><th style="font-weight:bold;font-size:20px;background:gray;text-align: center;">操作</th></tr>';
    if (info)
    {
        var genXmlUrl = "/yanxuan/findInfobyName?";
        var requestParam = "sysName="+info;
        $.get(genXmlUrl+requestParam,function(resdata)
        {
            var sysdata = $.parseJSON(resdata).data;
            // showaddSqlInfo('light',sqldata[datalenght]);
            for(datalenght = 0; datalenght < sysdata.length; datalenght++) {
                // console.log(sysdata[datalenght].addressees)
                tbody += "<tr><td id='sysid"+datalenght+"'>"+sysdata[datalenght].id+"</td><td id='sysname"+datalenght+"'>"+sysdata[datalenght].serverName+"</td><td style='word-break: break-all;word-wrap: break-word;' id='sysaddree"+datalenght+"'>"+sysdata[datalenght].addressees+"</td><td id='sysdel"+datalenght+"'><button class='btn-link' onclick='delSysInfo("+sysdata[datalenght].id+")'>删除</button><button id='sysedit"+datalenght+"' class=\"btn-link\" onclick=\"showaddSysInfo('lightSyseidt','"+sysdata[datalenght].id+"','"+sysdata[datalenght].serverName+"','"+sysdata[datalenght].addressees+"')\">修改</button></td></tr>";
            }
            $("#sysinfo").append(tbody);
        });
    }
    else
    {

        var genXmlUrl = "/yanxuan/getInfolimit?";
        var requestParam = "limitStart="+limitStart+"&limitEnd="+limitEnd;
        $.get(genXmlUrl+requestParam,function(resdata)
        {
            var sysdata = $.parseJSON(resdata).data;
            // console.log("test");
            for(datalenght = 0; datalenght < sysdata.length; datalenght++) {
                tbody += "<tr><td id='sysid"+datalenght+"'>"+sysdata[datalenght].id+"</td><td id='sysname"+datalenght+"'>"+sysdata[datalenght].serverName+"</td><td style='word-break: break-all;word-wrap: break-word;' id='sysaddree"+datalenght+"'>"+sysdata[datalenght].addressees+"</td><td id='sysdel"+datalenght+"'><button class='btn-link' onclick='delSysInfo("+sysdata[datalenght].id+")'>删除</button><button id='sysedit"+datalenght+"' class=\"btn-link\" onclick=\"showaddSysInfo('lightSyseidt','"+sysdata[datalenght].id+"','"+sysdata[datalenght].serverName+"','"+sysdata[datalenght].addressees+"')\">修改</button></td></tr>";
            }
            $("#sysinfo").append( tbody );
        });
    }
}

function addSysInfo() {
    var sysName = $('#sysname').val();
    var sysAddress = encodeURIComponent($('#addressinfoid').val());
    if (sysName=="" || sysAddress=="")
    {
        alert("每个参数都需要填写");
        return;
    }
    var insertUrl = "/yanxuan/insertInfo?";
    var requestParam = "serverName="+sysName+"&addressees="+sysAddress;
    $.get(insertUrl+requestParam,function(data){
        if (data==1)
        {
            alert("新增成功");
            location.reload([true]);
        }
        else{
            alert("新增失败,请联系管理员");
        }
    });

}

// setValue('sqlid0','sqlnameeidt')
function setValue(inid,outid){
    document.getElementById(outid).value= document.getElementById (inid).value;
}

function updateSysInfo(id) {
    var sysName = $('#sysnameeidt').val();
    var sysAddress = encodeURIComponent($('#addressinfoeidtid').val());
    if (sysName=="" || sysAddress=="")
    {
        alert("每个参数都需要填写");
        return;
    }
    var insertUrl = "/yanxuan/updateInfo?";
    var requestParam = "serverName="+sysName+"&addressees="+sysAddress+"&id="+id;
    $.get(insertUrl+requestParam,function(data){
        if (data.indexOf("200") > 0)
        {
            alert("修改成功");
            location.reload([true]);
        }
        else{
            alert("修改失败,请联系管理员");
        }
    });
}

function hide(tag){
    var light=document.getElementById(tag);
    var fade=document.getElementById('fade');
    light.style.display='none';
    fade.style.display='none';
}
function showaddSysInfo(tag,sysid,serverName,addressees){
    // $('#lightSyseidt').empty();
    var light=document.getElementById(tag);
    var fade=document.getElementById('fade');
    light.style.display='block';
    fade.style.display='block';
    // $('#lightSyseidt').empty();
    if (serverName !="")
    {

        document.getElementById('sysnameeidt').value=serverName;
        document.getElementById('addressinfoeidtid').value=addressees;
        $('#lightSyseidt').append("<button type=\"button\" id=\"sysaddeidtid\"  class=\"close\" onclick=updateSysInfo("+sysid+")>\n" +
            "                    <a href=\"javascript:void(0)\" onclick=\"hide('lightSyseidt')\">\n" +
            "                        提交\n" +
            "                    </a>\n" +
            "                </button>");
    }
}
//    delSqlInfo(0);
function delSysInfo(id) {
    var insertUrl = "/yanxuan/deleteInfo?";
    var requestParam = "id="+id;
    $.get(insertUrl+requestParam,function(data){
        if (data.indexOf("200") > 0)
        {
            alert("删除成功");
            location.reload([true]);
        }
        else{
            alert("删除失败,请联系管理员");
        }
    });

}
