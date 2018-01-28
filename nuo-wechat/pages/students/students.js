// pages/students/students.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    students:{}
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    var cId = options.cId;
    var sBId = options.sBId;

    that.setData({
      sBId: sBId
    });
    var ticket = wx.getStorageSync('ticket');
    that.getStudents(cId,ticket);

  },
  getStudents(cId, ticket) {
    var that = this;
    wx.request({
      url: 'https://www.kehue.com/nuo/students?classId='+cId,
      header: {
        ticket: ticket
      },
      method: 'GET',
      success: function (res) {
        //移除列表中下标为index的项
        var data = res.data;
        if (data.code == 200) {
          var students = data.data;
          for (var i = 0; i < students.length; i++) {
            var student = students[i];
            student.birthday = that.formatDate(student.birthday);
          }
          that.setData({
            students: students
          });
          that.getScore(cId,ticket);
          console.log("学生");
          console.log(that.data.students);
        } else {
          console.log("获取到的ticket过期了");
          that.setData({
            message: "登录过期，请重新登录"
          });
        }
      }
    })
  },
  getScore(cId, ticket) {
    var that = this;
    var sBId = that.data.sBId;
    wx.request({
      url: 'https://www.kehue.com/nuo/scores?classId='+cId+'&type=1&scoreBatchId='+sBId,
      header: {
        ticket: ticket
      },
      method: 'GET',
      success: function (res) {
        //移除列表中下标为index的项
        var data = res.data;
        if (data.code == 200) {
          var scores = data.data;
          console.log("这个批次的成绩单");
          console.log(scores);
          var students = that.data.students;
          for(var j=0;j<students.length;j++){
            students[j].scoreType=1;
            for (var i = 0; i < scores.length; i++) {
                var score = scores[i];
                if (score.studentId == students[j].id) {
                students[j].score = score;
                students[j].scoreType = 2;
              }
            }
          }
          that.setData({
            students: students
          });
          console.log("学生");
          console.log(that.data.students);
        } else {
          console.log("获取到的ticket过期了");
          that.setData({
            message: "登录过期，请重新登录"
          });
        }
      }
    })
  },
  formatDate: function (date) {
    return date.substring(0, 10);
  },
  uplodScore: function (e) {
    console.log(e);
    var date = e.target.dataset;
    var sId = date.sid;
    var sBId = this.data.sBId;
    wx.navigateTo({
      url: '../uplodScore/uplodScore?sId=' + sId+'&sBId='+sBId
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