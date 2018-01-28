// pages/batchM/batchM.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    message: "未查得数据！",
    isMessage: "hide",
    batchs: {},
    exams:{},
    isLoad: false,
    role: 0,
    grade:[
      {id:1,name:"一年级",cl:"xx"},
      { id: 2, name: "二年级", cl: "xx" },
      { id: 3, name: "三年级", cl: "xx" },
      { id: 4, name: "四年级", cl: "xx" },
      { id: 5, name: "五年级", cl: "xx" },
      { id: 6, name: "六年级", cl: "xx" },
      { id: 7, name: "七年级", cl: "cx" },
      { id: 8, name: "八年级", cl: "cx" },
      { id: 9, name: "九年级", cl: "cx" },
      { id: 10, name: "高一", cl: "gx" },
      { id: 11, name: "高二", cl: "gx" },
      { id: 12, name: "高三", cl: "gx" }
    ],
    subject:[
      { id: 1 ,name:"语文"},
      {id:2,name:"数学"},
      {id:3,name:"英语"},
      {id:4,name:"地理"},
      { id: 5, name: "政治" },
      { id: 6, name: "物理" },
      { id: 7, name: "化学" },
      { id: 8, name: "生物" },
      { id: 9, name: "文综" },
      { id: 10, name: "理综" }
    ]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    var role = wx.getStorageSync("role");
    var sId = options.sId;
    that.getExams(sId);
    that.getSchools(sId);
    that.setData({
      isLoad: true,
      role: role,
      schoolId:sId
    });
  },
  getExams: function (schoolId) {
    var that = this;
    var ticket = wx.getStorageSync('ticket');
    wx.request({
      url: 'https://www.kehue.com/nuo/exams?schoolId='+schoolId+'&page=1&pageSize=10',
      header: {
        ticket: ticket
      },
      method: 'GET',
      success: function (res) {
        var data = res.data;
        console.log("批次管理");
        console.log(data);
        if (data.code == 200) {
          var exams = data.data;
          if (exams.length >= 0) {
            for(var i=0;i<exams.length;i++){
              var exam = exams[i];
              var subs = exam.subjects;
              var subList = subs.split(",");              
              exams[i].subjectId = subList; 
            }
            that.setData({
              exams: exams,
              isMessage: "hide"
            });
          } else {
            that.setData({
              exams: {},
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
  deleteBatchModal: function (e) {
    const dataset = e.currentTarget.dataset;
    var value = dataset.index;
    var eId = dataset.eid;
    var that = this;
    wx.showModal({
      content: '删除后，将清空该批次下所有信息！',
      success: function (res) {
        if (res.confirm) {
          that.deleteBatch(value, eId);
        }
      }
    })
  },
  deleteBatch: function (value, eId) {
    var that = this;
    var ticket = wx.getStorageSync('ticket');
    var exams = that.data.exams;
    wx.request({
      url: 'https://www.kehue.com/nuo/exams?id=' + eId,
      header: {
        ticket: ticket
      },
      method: 'DELETE',
      success: function (res) {
        var data = res.data;
        console.log(data);
        if (data.code == 200) {
          exams.splice(value, 1);
          that.setData({
            exams: exams
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
  getSchools(sid) {
    var ticket = wx.getStorageSync('ticket');
    var that = this;
    wx.request({
      url: 'https://www.kehue.com/nuo/schools?id=' + sid,
      header: {
        ticket: ticket
      },
      method: 'GET',
      success: function (res) {
        var exams = that.data.exams;
        //移除列表中下标为index的项
        var data = res.data;
        if (data.code == 200) {
          var school = data.data[0];
          that.setData({
            schoolName: school.name,
            sType: school.type,
            schoolId: school.id
          });
        } else {
          console.log("获取到的ticket过期了");
          that.setData({
            message: "登录过期，请重新登录"
          });
        }
      }
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
    var sId = that.data.schoolId;
    if (!isLoad) {
      that.getExams(sId);
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