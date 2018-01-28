// pages/classM/classM.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    message: "未查得数据！",
    isMessage: "hide",
    classs: {},
    isLoad: false,
    role: 0
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    var role = wx.getStorageSync("role");
    that.getClass();
    that.setData({
      isLoad: true,
      role: role
    });
  },
  getClass: function () {
    var that = this;
    var ticket = wx.getStorageSync('ticket');
    wx.request({
      url: 'https://www.kehue.com/nuo/classes/managers',
      header: {
        ticket: ticket
      },
      method: 'GET',
      success: function (res) {
        var data = res.data;
        console.log("班级管理");
        console.log(data);
        if (data.code == 200) {
          var classList = data.data;
          if (classList.length >= 0) {
            that.setData({
              classs: classList,
              isMessage: "hide"
            });
          } else {
            that.setData({
              classs: {},
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
  deleteClassModal: function (e) {
    const dataset = e.currentTarget.dataset;
    var value = dataset.index;
    var cId = dataset.cid;
    var that = this;
    wx.showModal({
      content: '删除后，将清空该班级下所有信息！',
      success: function (res) {
        if (res.confirm) {
          that.deleteClass(value, cId);
        }
      }
    })
  },
  deleteClass: function (value, cId) {
    var that = this;
    var ticket = wx.getStorageSync('ticket');
    var classs = that.data.classs;

    wx.request({
      url: 'https://www.kehue.com/nuo/classes?id=' + cId,
      header: {
        ticket: ticket
      },
      method: 'DELETE',
      success: function (res) {
        var data = res.data;
        console.log(data);
        if (data.code == 200) {
          that.getClass();
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
      that.getClass();
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