// pages/studentM/studentM.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    message: "未查得数据！",
    isMessage: "hide",
    students: {},
    isLoad: false,
    role: 0,
    sId:0,
    cId:0
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    if (!that.data.isLoad) {
      that.setData({
        isLoad: true,
        sId: options.sId,
        cId: options.cId
      }); 
      that.getStudent();
    }
  },
  getStudent: function () {
    var that = this;
    var ticket = wx.getStorageSync('ticket');
    var cId = that.data.cId;
    wx.request({
      url: 'https://www.kehue.com/nuo/students?classId='+cId,
      header: {
        ticket: ticket
      },
      method: 'GET',
      success: function (res) {
        var data = res.data;
        console.log("学生管理");
        console.log(data);
        if (data.code == 200) {
          var studentList = data.data;
          if (studentList.length >= 0) {
            that.setData({
              students: studentList,
              isMessage: "hide"
            });
          } else {
            that.setData({
              students: {},
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
  deleteStudentModal: function (e) {
    const dataset = e.currentTarget.dataset;
    var value = dataset.index;
    var sId = dataset.sid;
    var that = this;
    wx.showModal({
      content: '删除后，将清空该学生下所有信息！',
      success: function (res) {
        if (res.confirm) {
          that.deleteStudent(value, sId);
        }
      }
    })
  },
  deleteStudent: function (value, sId) {
    var that = this;
    var ticket = wx.getStorageSync('ticket');
    var students = that.data.students;
    wx.request({
      url: 'https://www.kehue.com/nuo/students?id=' + sId,
      header: {
        ticket: ticket
      },
      method: 'DELETE',
      success: function (res) {
        var data = res.data;
        console.log("学校管理");
        console.log(data);
        if (data.code == 200) {
          students.splice(value, 1);
          that.setData({
            students: students
          });
        } else {
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
  editStudentModal:function(e){
    const dataset = e.currentTarget.dataset;
    var stuId = dataset.stuid;
    wx.navigateTo({
      url: '../addM/addM?stuId=' + stuId+"&type=2&edit=1"
    })
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
    var that = this;
    var isLoad = that.data.isLoad;
    if (!isLoad) {
      that.getStudent();
    }
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    this.setData({
      isLoad: false
    });
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