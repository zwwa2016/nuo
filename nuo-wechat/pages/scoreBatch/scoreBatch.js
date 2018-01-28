// pages/scoreBatch/scoreBatch.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    scoreBatch:{},
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
    ]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;  
    var cId = options.cId;
    var sId = options.sId;
    var ticket = wx.getStorageSync('ticket');
    this.getScoreBatch(cId,ticket);
  },
  getScoreBatch(cid, ticket) {
    var that = this;
    wx.request({
      url: 'https://www.kehue.com/nuo/scoreBatchs?classId='+cid+'&page=1&pageSize=10',
      header: {
        ticket: ticket
      },
      method: 'GET',
      success: function (res) {
        //移除列表中下标为index的项
        var data = res.data;
        if (data.code == 200) {
          var scoreBatchList = data.data;
          for(var i=0;i<scoreBatchList.length;i++){
            scoreBatchList[i].stateMes = that.getStateMes(scoreBatchList[i].state);
            that.getExams(ticket, scoreBatchList[i].examId);
          }
          that.setData({
            scoreBatch: scoreBatchList
          });
          console.log("考试批次");
          console.log(that.data.scoreBatch);
        } else {
          console.log("获取到的ticket过期了");
        }
      }
    })
  },
  getExams: function (ticket,eid){
    var that = this;
    wx.request({
      url: 'https://www.kehue.com/nuo//exams?id=' + eid ,
      header: {
        ticket: ticket
      },
      method: 'GET',
      success: function (res) {
        //移除列表中下标为index的项
        var data = res.data;
        if (data.code == 200) {
          var exams = data.data[0];
          var scoreBatchList = that.data.scoreBatch;
          for (var i = 0; i < scoreBatchList.length; i++) {
            if (scoreBatchList[i].examId == exams.id){
              scoreBatchList[i].examName = exams.name;
            }
          }
          that.setData({
            scoreBatch: scoreBatchList
          });
          console.log("考试批次");
          console.log(that.data.scoreBatch);
        } else {
          console.log("获取到的ticket过期了");
          
        }
      }
    })
  },
  getStateMes:function(state){
    if(state == 1){
      return "未完成";
    } else if(state == 2){
      return "等待更新统计";
    } else if(state == 3){
      return "已完成";
    }
  },
  inputScore: function (e) {
    console.log(e);
    var data = e.target.dataset;
    var sBId = data.sbid;
    var cId = data.cid;

    var sname = data.sname;
    wx.setStorageSync("sBName", sname);
    wx.navigateTo({
      url: '../students/students?sBId='+sBId+'&cId='+cId
    })
  },
  updateScoreBatch: function () {
    console.log("跳转到新增孩子页面");
    
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