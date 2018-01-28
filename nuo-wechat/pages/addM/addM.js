// pages/addM/addM.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    addType:0,
    urlList: ["/schools", "/classes", "/students", "/exams"],
    birthday: '2000-09-01',
    num: 0,
    pic: "",
    imageList: [],
    student:{},
    nan:true,
    nv:false,
    grades: [
      { id: 1, name: "一年级", cl: 1 },
      { id: 2, name: "二年级", cl: 1 },
      { id: 3, name: "三年级", cl: 1 },
      { id: 4, name: "四年级", cl: 1 },
      { id: 5, name: "五年级", cl: 1 },
      { id: 6, name: "六年级", cl: 1 },
      { id: 7, name: "七年级", cl: 2 },
      { id: 8, name: "八年级", cl: 2 },
      { id: 9, name: "九年级", cl: 2 },
      { id: 10, name: "高一", cl: 3 },
      { id: 11, name: "高二", cl: 3 },
      { id: 12, name: "高三", cl: 3 }
    ],
    subjects: [
      { id: 1, name: "语文" },
      { id: 2, name: "数学" },
      { id: 3, name: "英语" },
      { id: 4, name: "地理" },
      { id: 5, name: "政治" },
      { id: 6, name: "物理" },
      { id: 7, name: "化学" },
      { id: 8, name: "生物" },
      { id: 9, name: "文综" },
      { id: 10, name: "理综" }
    ],
    schoolTypes: [
      { id: 1, name: "小学"},
      { id: 2, name: "初中"},
      { id: 3, name: "高中"}
    ]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    var addType = options.type;
    var cId = options.cId;
    var sId = options.sId;
    var stuId = options.stuId;
    var edit = options.edit;
    var schoolId = options.schoolId;
    var sType = options.sType;
    that.setData({
      addType: addType,
      cId:cId,
      sId:sId,
      stuId: stuId,
      edit:edit,
      schoolId: schoolId,
      sType:sType
    })
    if(addType == 1){
      that.getSchools();
    } 
    if(edit == 1){
      that.getStudent(stuId);
    }
    
  },
  formSubmit: function (e) {
    console.log('form发生了submit事件，携带数据为：', e.detail.value)
    var value = e.detail.value;
    var that = this;
    var addType = that.data.addType;
    var ticket = wx.getStorageSync('ticket');
    var data = {};
    var edit = that.data.edit;
    if(addType == 0){
      var schoolName = value.schoolName;
      var schoolType = value.schoolType;
      if (schoolName == null || schoolName.length == 0 || schoolType == ""){
        that.toast();
        return;
      }
      data.name = schoolName;
      data.type = schoolType;
    } else if(addType == 1){
      var grade = value.grade;
      var num = value.num;
      var schoolId = value.schoolId;
      if(grade == "" || num == "" || schoolId == ""){
        that.toast();
        return;
      }
      data.grade = grade;
      data.number = num;
      data.schoolId = schoolId;
    } else if(addType == 2){
      var studentName = value.studentName;
      var sex = value.sex;
      var birthday = that.data.birthday;
      var imageList = that.data.imageList;
      if (studentName == "" || sex == ""){
        that.toast();
        return;
      }
      data.name = studentName;
      data.sex = sex;
      data.birthday = birthday +" 00:00:00";
      if(edit ==1){
        data.id = that.data.stuId;
      } else {
        data.schoolId = that.data.sId;
        data.classId = that.data.cId;
      }
    } else if(addType == 3){
      var name = value.examsName;
      var grade = value.grade;
      var subjectList = value.subject;
      var subjects = "";
      for (var i = 0; i < subjectList.length;i++){
        if (subjects == ""){
          subjects += subjectList[i];
        } else {
          subjects += ','+subjectList[i];
        }
      }
      data.name = name;
      data.grade = grade;
      data.subjects = subjects;
      data.schoolId = that.data.schoolId;
    }
    var editType = "POST";
    if(edit == 1){
      editType = "PUT"
    }
    that.addLoad(addType, data, ticket, editType);
  },
  toast:function(){
    wx.showModal({
      content: '填写完整后才能提交',
      showCancel:false,
      success: function (res) {
        if (res.confirm) {
          console.log('用户点击确定')
        } 
      }
    })
  },
  formReset: function () {
    console.log('form发生了reset事件')
  },
  bindDateChange: function (e) {
    console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      birthday: e.detail.value
    })
  },
  addLoad: function (addType, data, ticket, editType) {
    wx.showLoading({
      mask: true,
      title: '加载中',
    });
    var that = this;
    if (addType == 2) {
      var imageList = that.data.imageList;
      if(imageList.length == 0){
        that.add(that, addType, data, ticket, editType);
      } else {
        that.uplodImage(that, ticket, imageList[0]);
        var intervalId = setInterval(function () {
          var pic = that.data.pic;
          if (pic != "") {
            data.pic = pic;
            clearInterval(intervalId);
            that.add(that, addType, data, ticket, editType);
          }
        }, 300);
      }
    } else {
      that.add(that, addType, data, ticket, editType);
    }
  },
  add: function (that, addType, data, ticket, editType) {
    var urlList = that.data.urlList;
    console.log(urlList[addType]);
    console.log(data);
    wx.request({
      url: 'https://www.kehue.com/nuo' + urlList[addType],
      header: {
        ticket: ticket
      },
      data: data,
      method: editType,
      success: function (res) {
        var data = res.data;
        console.log("新增结果");
        console.log(data);
        if (data.code == 200) {
          wx.hideLoading();
          wx.navigateBack();
        } else {
          wx.hideLoading();
          wx.showModal({
            content: data.desc,
            showCancel: false,
            success: function (res) {
              if (res.confirm) {
                console.log('用户点击确定')
              }
            }
          })
        }
      }
    })
  },
  getSchools: function () {
    var that = this;
    var ticket = wx.getStorageSync('ticket');
    wx.request({
      url: 'https://www.kehue.com/nuo/schools/managers',
      header: {
        ticket: ticket
      },
      method: 'GET',
      success: function (res) {
        var data = res.data;
        console.log("获取到学校");
        console.log(data);
        if (data.code == 200) {
          var schoolList = data.data;
          if (schoolList.length >= 0) {
            that.setData({
              schools: schoolList,
              isMessage: "hide"
            });
          } else {
            that.setData({
              schools: {},
              isMessage: "show",
              message: "未查得数据！"
            });
          }
        } else {
          that.setData({
            message: "登录过期，请重新登录"
          });
        }
      }
    })
  },
  getStudent: function (stuId) {
    var that = this;
    var ticket = wx.getStorageSync('ticket');
    var cId = that.data.cId;
    wx.request({
      url: 'https://www.kehue.com/nuo/students?id=' + stuId,
      header: {
        ticket: ticket
      },
      method: 'GET',
      success: function (res) {
        var data = res.data;
        console.log("单个学生");
        console.log(data);
        if (data.code == 200) {
          var studentList = data.data;
          if (studentList.length >= 0) {
            var student = studentList[0];
            var nan = false;
            var nv = false;
            if(student.sex == 1){
              nan = true;
            } else {
              nv = true;
            }
            that.setData({
              student: student,
              birthday: that.formatDate(student.birthday),
              nan:nan,
              nv:nv
            });
          } else {
            that.setData({
              student: {}
            });
          }
        } else {
          that.setData({
            message: "登录过期，请重新登录"
          });
        }
      }
    })
  },
  chooseImage: function () {
    var that = this
    var imageList = [];
    wx.chooseImage({
      count: 1,
      success: function (res) {
        console.log(res)
        var list = res.tempFilePaths;
        imageList.push(list[0]);
        that.setData({
          imageList: imageList,
        })
      }
    })
  },
  previewImage: function (e) {
    var current = e.target.dataset.src

    wx.previewImage({
      current: current,
      urls: this.data.imageList
    })
  },
  uplodImage: function (that, ticket, imagePath) {
    const uploadTask = wx.uploadFile({
      url: 'https://www.kehue.com/nuo/files', //仅为示例，非真实的接口地址
      filePath: imagePath,
      header: {
        ticket: ticket
      },
      name: 'file',
      success: function (res) {
        var data = res.data
        data = JSON.parse(data);
        var pic = data.url;
        that.setData({
          pic: pic
        });
      }
    })
    uploadTask.onProgressUpdate((res) => {
      console.log( "当前进度：" + res.progress);
    })
  },
  formatDate: function (date) {
    return date.substring(0, 10);
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
  
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
  
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
  
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
  
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
  
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
  
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
  
  }
})