joinPage("");
function joinPage(info){
    $("#paginationinfo").empty();
    if (info)
    {
        lbody += "<li class=\"pagination pagination-item-active\"><a>1</a></li>";
        $("#paginationinfo").append(lbody);
    }
    else{
        var genXmlUrl = "/getLimitSqlInfo?";
        var requestParam = "limitStart=0&limitEnd=10";
        var lbody='';
        $.get(genXmlUrl+requestParam,function(resdata){
            var sqlcount = $.parseJSON(resdata).count;
            for(temp = 1; temp <= Math.ceil(sqlcount/10); temp++)
            {
                lbody += "<li class=\"pagination\"><a  id='pagecoutid"+temp+"'onclick=\"javascript:selectSqlInfo("+(temp-1)*10+","+10+");\">"+temp+"</a></li>";
            }
            $("#paginationinfo").append(lbody)
        })
    }
}

selectSqlInfo(0,10,'');
function selectSqlInfo(limitStart,limitEnd,info) {
    $("#sqlinfo").empty();
    $("#pagecoutid"+Math.ceil((limitStart+1)/10)).toggleClass("pagination pagination-item-active");
    var tablestart = "<table border='1' align=\"center\">";
    var tableend = "</table>";
    var tbody = '<tr class="tablehead"><th style="font-weight:bold;font-size:20px;background:gray;text-align: center;">id</th><th style="font-weight:bold;font-size:20px;background:gray;text-align: center;" >数据库名</th><th style="font-weight:bold;font-size:20px;background:gray;text-align: center;">数据库类型</th><th style="font-weight:bold;font-size:20px;background:gray;text-align: center;">连接信息</th><th style="font-weight:bold;font-size:20px;background:gray;text-align: center;">用户名</th><th style="font-weight:bold;font-size:20px;background:gray;text-align: center;">密码</th><th style="font-weight:bold;font-size:20px;background:gray;text-align: center;">操作</th></tr>';
    if (info)
    {
        var genXmlUrl = "/getSqlInfobyinfo?";
        var requestParam = "sqlconninfo="+info;
        $.get(genXmlUrl+requestParam,function(resdata)
        {
            var sqldata = $.parseJSON(resdata).data;
            showaddSqlInfo('light',sqldata[datalenght]);
            for(datalenght = 0; datalenght < sqldata.length; datalenght++) {
                console.log(sqldata[datalenght])
                tbody += "<tr><td id='sqlid"+datalenght+"'>"+sqldata[datalenght].id+"</td><td id='sqlname"+datalenght+"'>"+sqldata[datalenght].sqlname+"</td><td id='sqlmode"+datalenght+"'>"+sqldata[datalenght].sqlmode+"</td><td id='sqlconninfo"+datalenght+"'>"+sqldata[datalenght].sqlconninfo+"</td><td id='sqlusername"+datalenght+"'>"+sqldata[datalenght].sqlusername+"</td><td id='sqlpassword"+datalenght+"'>"+sqldata[datalenght].sqlpassword+"</td><td id='sqldel"+datalenght+"'><button class='btn-link' onclick='delSqlInfo("+sqldata[datalenght].id+")'>删除</button><button id='sqledit"+datalenght+"' class=\"btn-link\" onclick=\"showaddSqlInfo('light',"+sqldata[datalenght]+")\">修改</button></td></tr>";
            }
            $("#sqlinfo").append(tablestart + tbody + tableend);
        });
    }
    else
    {
        var genXmlUrl = "/getLimitSqlInfo?";
        var requestParam = "limitStart="+limitStart+"&limitEnd="+limitEnd;
        $.get(genXmlUrl+requestParam,function(resdata)
        {
            var sqldata = $.parseJSON(resdata).data;
            var test132 = "test79465";
            console.log(sqldata);
            for(datalenght = 0; datalenght < sqldata.length; datalenght++) {
                tbody += "<tr><td id='sqlid"+datalenght+"'>"+sqldata[datalenght].id+"</td><td id='sqlname"+datalenght+"'>"+sqldata[datalenght].sqlname+"</td><td id='sqlmode"+datalenght+"'>"+sqldata[datalenght].sqlmode+"</td><td id='sqlconninfo"+datalenght+"'>"+sqldata[datalenght].sqlconninfo+"</td><td id='sqlusername"+datalenght+"'>"+sqldata[datalenght].sqlusername+"</td><td id='sqlpassword"+datalenght+"'>"+sqldata[datalenght].sqlpassword+"</td><td id='sqldel"+datalenght+"'><button class='btn-link' onclick='delSqlInfo("+sqldata[datalenght].id+")'>删除</button><button id='sqledit"+datalenght+"' class=\"btn-link\" onclick=\"showaddSqlInfo('lighteidt','"+sqldata[datalenght].id+"','"+sqldata[datalenght].sqlname+"','"+sqldata[datalenght].sqlmode+"','"+sqldata[datalenght].sqlconninfo+"','"+sqldata[datalenght].sqlusername+"','"+sqldata[datalenght].sqlpassword+"')\">修改</button></td></tr>";
            }
            $("#sqlinfo").append(tablestart + tbody + tableend);
        });
    }
}

function addSqlInfo() {
    var sqlmode = $('#sqlmodeid').val();
    var sqlinfo = $('#sqlinfoid').val();
    var sqlusername = $('#sqlusernameid').val();
    var sqlpassword = $('#sqlpasswordid').val();
    var sqlname = $('#sqlname').val();
    if (sqlmode=="" || sqlinfo=="" || sqlusername=="" || sqlpassword=="" || sqlname=="")
    {
        alert("每个参数都需要填写");
        return;
    }
    var insertUrl = "/insertSql?";
    var requestParam = "sqlmode="+sqlmode+"&sqlconninfo="+sqlinfo+"&sqlusername="+sqlusername+"&sqlpassword="+sqlpassword+"&sqlname="+sqlname;
    $.get(insertUrl+requestParam,function(data){
        if (data==1)
        {
            alert("新增成功");
            location.reload([true]);
        }
        else{
            alert("新增失败,连接信息必须按照标准信息进行输入");
        }
    });

}

// setValue('sqlid0','sqlnameeidt')
function setValue(inid,outid){
    document.getElementById(outid).value= document.getElementById (inid).value;
}

function updateSqlInfo(id) {
    var sqlmode = $('#sqlmodeeidtid').val();
    var sqlinfo = $('#sqlinfoeidtid').val();
    // var sqlinfo = document.getElementById("sqlinfoid").value;
    var sqlusername = $('#sqlusernameeidtid').val();
    var sqlpassword = $('#sqlpasswordeidtid').val();
    var sqlname = $('#sqlnameeidt').val();
    // var sqlname = document.getElementById("sqlnameeidt").value;
    console.log("sqlname="+sqlname)
    console.log("sqlmode="+sqlmode+"&sqlinfo="+sqlinfo+"&sqlusername="+sqlusername+"&sqlpassword="+sqlpassword+"&sqlname="+sqlname)
    if (sqlmode=="" || sqlinfo=="" || sqlusername=="" || sqlpassword=="" || sqlname=="")
    {
        alert("每个参数都需要填写");
        return;
    }
    var insertUrl = "/updateSql?";
    var requestParam = "sqlmode="+sqlmode+"&sqlconninfo="+sqlinfo+"&sqlusername="+sqlusername+"&sqlpassword="+sqlpassword+"&sqlname="+sqlname+"&id="+id;
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
function showaddSqlInfo(tag,sqlid,sqlname,sqlmode,sqlconninfo,sqluser,sqlpassword){
    console.log(sqluser);
    console.log(sqlpassword)
    var light=document.getElementById(tag);
    var fade=document.getElementById('fade');
    light.style.display='block';
    fade.style.display='block';
    if (sqlname !="")
    {
        document.getElementById('sqlnameeidt').value=sqlname;
        document.getElementById('sqlmodeeidtid').value=sqlmode;
        document.getElementById('sqlinfoeidtid').value=sqlconninfo;
        document.getElementById('sqlusernameeidtid').value=sqluser;
        document.getElementById('sqlpasswordeidtid').value=sqlpassword;
        $('#lighteidt').append("<button type=\"button\" id=\"sqladdeidtid\"  class=\"close\" onclick=updateSqlInfo("+sqlid+")>\n" +
            "                    <a href=\"javascript:void(0)\" onclick=\"hide('lighteidt')\">\n" +
            "                        提交\n" +
            "                    </a>\n" +
            "                </button>");
        // $('#sqladdeidtid').append("onclick='updateSqlInfo("+sqlid+")'");
        location.reload([true]);
    }
}
//    delSqlInfo(0);
function delSqlInfo(id) {
    var insertUrl = "/deleteSql?";
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
