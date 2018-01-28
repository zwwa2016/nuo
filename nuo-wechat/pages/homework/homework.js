// pages/homework/homework.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    errorShow: "hide",
    message:"暂无记录",
    score:[],
    sId:""
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    that.setData({
      sId: options.sId
    })    
    that.requestData(that.data.sId);
  },
  requestData: function (sId) {
    var that = this;
    var ticket = wx.getStorageSync('ticket');
    console.log("成绩单页面ticket：" + ticket);
    wx.request({
      url: 'https://www.kehue.com/nuo/scores?studentId='+sId+'&type=2&page=1&pageSize=10',
      header: {
        ticket: ticket
      },
      method: 'GET',
      success: function (res) {
        console.log("获取作业");
        console.log(res);
        var data = res.data;
        var scoreList = data.data;
        if (data.code == 200) {
          for (var i = 0; i < scoreList.length; i++) {
            scoreList[i].pics = scoreList[i].pic.split(",");
          }
          if(scoreList.length > 0){
            for (var i = 0; i < scoreList.length; i++) {
              scoreList[i].pics = scoreList[i].pic.split(",");
            }
            that.setData({
              score: scoreList
            });
            console.log(that.data.score);
          } else {
            that.setData({
              errorShow: 'show'
            });
          }
        } else {
          console.log("获取到的ticket过期了");
          that.setData({
            errorShow: 'show',
            message: "登录过期，请重新登录。"
          });
        }
      }
    })
  },
  viewScore: function (e) {
    console.log(e);
    var dataset = e.currentTarget.dataset;
    var pics = dataset.pics;
    console.log(pics)
    wx.previewImage({
      current: pics[0], // 当前显示图片的http链接
      urls: pics // 需要预览的图片http链接列表
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