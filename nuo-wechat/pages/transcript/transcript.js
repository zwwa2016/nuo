var wxCharts = require('../../utils/wxcharts.js');
// pages/transcript/transcript.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    sId:"",
    errorShow:"hide",
    message:"暂不记录"
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    that.setData({
      sId: options.sId
    })
    that.onCanvas();
    that.requestData(that.data.sId);
  },

  onCanvas: function(){
    let windowWidth = 320;
    try {
      let res = wx.getSystemInfoSync();
      windowWidth = res.windowWidth;
    } catch (e) {
      console.error("获取屏幕信息异常");
    }
    new wxCharts({
      canvasId: 'wxChartCanvas',
      type: 'line',
      categories: ['7月', '8月', '9月', '10月', '11月', '12月'],
      series: [{
        name: '班级平均分',
        data: [60, 65.5, 70.5, 71.5, 72, 75]
      }, {
        name: '小明',
        data: [50, 60, 73.5, 78.5, 80, 88]
      }],
      yAxis: {
        title: '成绩单（分）',
        format: function (val) {
          return val.toFixed(2);
        },
        min: 30,
        max: 100
      },
      width: windowWidth,
      height: 200
    });
  },
  requestData: function (sId) {
    var that = this;
    var ticket = wx.getStorageSync('ticket');
    console.log("成绩单页面ticket："+ticket);
    wx.request({
      url: 'https://www.kehue.com/nuo/scores?studentId='+sId+'&type=1&page=1&pageSize=10',
      header: {
        ticket: ticket
      },
      method: 'GET',
      success: function (res) {
        console.log("获取成绩单");
        console.log(res);
        var data = res.data;
        var scoreList = data.data;
        if (data.code == 200) {
          if(scoreList.length > 0){
            for(var i=0;i<scoreList.length;i++){
              scoreList[i].pics = scoreList[i].pic.split(",");
            }
            that.setData({
              score: scoreList
            });
          } else {
            that.setData({
              errorShow: "show"
            });
          }
          
        } else {
          that.setData({
            errorShow: "show",
            message: "登录过期，请重新登录。"
          });
          console.log("获取到的ticket过期了");
        }
      }
    })
  },
  viewScore:function(e){
    console.log(e);
    var dataset = e.currentTarget.dataset;
    var pics = dataset.pics;
    console.log("查看图片");
    console.log(pics);
    
    var pic = pics[0];
    console.log(pic)
    wx.previewImage({
      current: ""+pic, // 当前显示图片的http链接
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