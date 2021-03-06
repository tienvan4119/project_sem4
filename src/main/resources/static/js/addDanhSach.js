$(document).on('change','.up', function(){
    var names = [];
    var length = $(this).get(0).files.length;
    for (var i = 0; i < $(this).get(0).files.length; ++i) {
        names.push($(this).get(0).files[i].name);
    }
    // $("input[name=file]").val(names);
    if(length>2){
        var fileName = names.join(', ');
        $(this).closest('.form-group').find('.form-control').attr("value",length+" files selected");
    }
    else{
        $(this).closest('.form-group').find('.form-control').attr("value",names);
    }
});

$('#submit-file').on("click",function(e){
    e.preventDefault();
    if($('#fileUpload').val() == "" || $('#listCourse option:selected').text().trim() == "Course...")
    {
        alert("Kiem tra ten hoc phan va file da chon")
    }
    else
    {
        $('#up').parse({
            config: {
                delimiter: "auto",
                complete: displayHTMLTable,
            },
            before: function(file, inputElem)
            {
                //console.log("Parsing file...", file);
            },
            error: function(err, file)
            {
                //console.log("ERROR:", err, file);
            },
            complete: function()
            {
                //console.log("Done with all files");
            }
        });
    }
});

$('#submit-file-teacher').on("click",function(e){
    e.preventDefault();
    if($('#fileUpload').val() == "")
    {
        alert("Kiem tra ten hoc phan va file da chon")
    }
    else
    {
        $('#up').parse({
            config: {
                delimiter: "auto",
                complete: displayHTMLTableTeacher,
            },
            before: function(file, inputElem)
            {
                //console.log("Parsing file...", file);
            },
            error: function(err, file)
            {
                //console.log("ERROR:", err, file);
            },
            complete: function()
            {
                //console.log("Done with all files");
            }
        });
    }
});

$('#submit-file-relationCourseTeacher').on("click",function(e){
    console.log("fck me")
    e.preventDefault();
    $('#up').parse({
        config: {
            delimiter: "auto",
            complete: displayHTMLTableTeacherCourse,
        },
        before: function(file, inputElem)
        {
            //console.log("Parsing file...", file);
        },
        error: function(err, file)
        {
            //console.log("ERROR:", err, file);
        },
        complete: function()
        {
            //console.log("Done with all files");
        }
    });

});



function displayHTMLTable(results) {
    var table = "<table class='table'>";
    var data = results.data;
    var course_name = $('#listCourse option:selected').text();
    for (i = 1; i < data.length; i++) {
        table += "<tr>";
        var row = data[i];
        var listData = String(row);
        // LẤY DATA CẦN CHÍNH LÀ ROW BẮT ĐẦU TỪ data[1];
        if (row[0] === "") continue;
        console.log(listData);
        console.log(typeof listData)
        console.log(i)
        console.log("Ah yes")
        $.ajax({
            type: "POST", url: "/api/admin/addStudentByFile", dataType: 'json',
            data: {
                course_name:course_name,
                listData : listData
            }, success: function (result) {
                $.ajax({
                    type: "POST", url: "/api/admin/getListStudentofCourseClass", dataType: 'json',
                    data: {
                        course_name: course_name
                    }, success: function (dataClass) {
                        $('#fileUpload').empty()
                        $('#students').empty()
                        var i;
                        for (i = 0; i < result.length; i++) {
                            console.log(result[i].firstName + result[i].lastName)
                            console.log(result[i].email)
                            console.log(result[i].location)
                            console.log(dataClass[i].className)
                            $('#students').append("<tr>" +
                                "<td>" + result[i].mssv + "</td>" +
                                "<td>" + result[i].firstName + " " + result[i].lastName + "</td>" +
                                "<td>" + result[i].location + "</td>" +
                                "<td>" + result[i].email + "</td>" +
                                "<td>" + dataClass[i].className + "</td>" +
                                "<td>" +
                                "<a onclick='editStudent(this.id)' class='edit' title='Edit' id='" + result[i].email + "' data-toggle='tooltip'><i class='material-icons'>&#xE254;</i></a>" +
                                "<a onclick='deleteStudent(this.id)' id='" + result[i].email + "' class='delete' title='Delete' data-toggle='tooltip'><i class='material-icons'>&#xE872;</i></a>" +
                                "</td>" +
                                "</tr>")
                        }
                    }
                })
            }
        })
        //     var cells = row.join(",").split(",");
        //     if (cells[0] === "") continue;
        //     for(j=0;j<cells.length;j++){
        //         table+= "<td>";
        //         //console.log(cells[j]);
        //         table+= cells[j];
        //         table+= "</th>";
        //     }
        //     table+= "</tr>";
        // }
        // table+= "</table>";
        // $("#students").html(table);
    }
}

function displayHTMLTableTeacher(results) {
    var table = "<table class='table'>";
    var data = results.data;
    var course_name = $('#listCourse option:selected').text();
    for (i = 1; i < data.length; i++) {
        table += "<tr>";
        var row = data[i];
        var listData = String(row);
        // LẤY DATA CẦN CHÍNH LÀ ROW BẮT ĐẦU TỪ data[1];
        if (row[0] === "") continue;
        console.log(listData);
        console.log(typeof listData)
        console.log(i)
        console.log("Ah yes")
        $.ajax({
            type: "POST", url: "/api/admin/addTeacherByFile", dataType: 'json',
            data: {
                course_name:course_name,
                listData : listData
            }, success: function (result) {

                console.log(result)
                var k = 0;
                for(k ; k<result.length;k++)
                {
                    $('#listTeachers').append("<option  value= "+result[k].email+" >" +result[k].firstName+" "+result[k].lastName+ "</option>");
                }

                $.ajax({type: "POST", url: "/api/admin/GetFullClass", dataType: 'json',
                    data : {

                    },success: function(dataClass){
                        $('#teachers').empty()
                        console.log(result)
                        var i;
                        for (i = 0; i < result.length; i++) {
                            console.log(result[i].firstName+ result[i].lastName)
                            console.log(result[i].email)
                            console.log(result[i].location)
                            console.log(dataClass[i].className)

                            $('#teachers').append("<tr>" +
                                "<td>"+result[i].firstName+" "+result[i].lastName+"</td>" +
                                "<td>"+result[i].location+"</td>" +
                                "<td>"+result[i].email+"</td>" +
                                "<td>"+dataClass[i].className+"</td>" +
                                "<td>" +
                                "<a onclick='editTeacher(this.id)' class='edit' title='Edit' id='"+result[i].email+"' data-toggle='tooltip'><i class='material-icons'>&#xE254;</i></a>" +
                                "<a onclick='deleteTeacher(this.id)' id='"+result[i].email+"' class='delete' title='Delete' data-toggle='tooltip'><i class='material-icons'>&#xE872;</i></a>" +
                                "</td>" +
                                "</tr>")
                        }
                    }})
            }
        })

    }
}

function displayHTMLTableTeacherCourse(results) {
    console.log("yes")
    var table = "<table class='table'>";
    var data = results.data;
    for (i = 1; i < data.length; i++) {
        table += "<tr>";
        var row = data[i];
        var listData = String(row);
        // LẤY DATA CẦN CHÍNH LÀ ROW BẮT ĐẦU TỪ data[1];
        if (row[0] === "") continue;
        console.log(listData);
        console.log(typeof listData)
        console.log(i)
        console.log("Ah yes")
        $.ajax({
            type: "POST", url: "/api/admin/addTeacherCourseByFile", dataType: 'json',
            data: {
                listData : listData
            }, success: function (course) {
                location.reload()
            }
        })

    }
}
