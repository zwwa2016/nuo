// pages/schoolM/schoolM.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    message:"未查得数据！",
    isMessage:"hide",
    schools:{},
    isLoad:false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    if(!that.data.isLoad){
      that.getSchools();
      that.setData({
        isLoad:true
      });
    }
  },
  getSchools:function(){
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
        console.log("学校管理");
        console.log(data);
        if (data.code == 200) {
          var schoolList = data.data;
          if(schoolList.length >= 0){
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
  deleteSchoolModal:function(e){
    const dataset = e.currentTarget.dataset;
    var value = dataset.index;
    var sId = dataset.sid;
    var that = this;
    wx.showModal({
      content: '删除后，将清空该学校下所有信息！',
      success: function (res) {
        if (res.confirm) {
          that.deleteSchool(value,sId);
        }
      }
    })
  },
  deleteSchool: function (value,sId) {
    var that = this;
    var ticket = wx.getStorageSync('ticket');
    var schools = that.data.schools;
    
    wx.request({
      url: 'https://www.kehue.com/nuo/schools?id='+sId,
      header: {
        ticket: ticket
      },
      method: 'DELETE',
      success: function (res) {
        var data = res.data;
        console.log("学校管理");
        console.log(data);
        if (data.code == 200) {
          schools.splice(value, 1);
          that.setData({
            schools: schools
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
      that.getSchools();
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