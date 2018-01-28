// pages/findChild/findChild.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    childs:[],
    errorShow:'show',
    message:'查找您的孩子，并绑定',
    relation: [
      { ix: 0, name: "自己" }, 
      { ix: 1, name: "父亲" }, 
      { ix: 2, name: "母亲" }, 
      { ix: 3, name: "爷爷" }, 
      { ix: 4, name: "奶奶" }, 
      { ix: 5, name: "姑姑" }, 
      { ix: 6, name: "姑父" }, 
      { ix: 7, name: "姨" }, 
      { ix: 8, name: "姨父" }, 
      { ix: 9, name: "创建者"}
    ],
    ix:0
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    
  },
  findChild:function(data){
    console.log("触发了输入事件");
    console.log(data.detail.value);
    var name = data.detail.value;
    var that = this;
    var ticket = wx.getStorageSync('ticket');
    console.log("新增孩子页面ticket：" + ticket);
    console.log("name:"+name);
    wx.request({
      url: 'https://www.kehue.com/nuo/students?name='+name,
      header: {
        "ticket": ticket
      },
      method: 'GET',
      success: function (res) {
        console.log("获取我的孩子");
        console.log(res);
        var data = res.data;
        if (data.code == 200) {
          var childList = data.data;
          if(childList.length != 0){
            for (var i = 0; i < childList.length; i++) {
              childList[i].birthday = that.formatDate(childList[i].birthday);
              childList[i].bindState = true;
              that.getSchools(childList[i].schoolId, ticket);
              that.getClass(childList[i].classId, ticket);
            }
            that.setData({
              childs: childList,
              errorShow: "hide"
            });
          } else {
            that.setData({
              childs:[],
              errorShow:"show",
              message: "您搜索的孩子 " + name +" 不存在" 
            });
          }
        } else {
          console.log("获取到的ticket过期了");
          that.setData({
            message: "登录过期，请重新登录"
          });
        }
      }
    })
  },
  formatDate:function (date) {
    return date.substring(0, 10);
  },
  bindPickerChange: function (e) {
    var that = this;
    console.log('picker发送选择改变，携带值为', e.detail.value)
    that.setData({
      ix: e.detail.value
    })
    console.log(this.data.ix);
  },
  bindChild: function(e){
    const dataset = e.currentTarget.dataset;
    console.log(e)
    console.log("孩子id：" + dataset.id);
    var that = this;
    var ticket = wx.getStorageSync('ticket');
    var sId = dataset.id;
    var ix = dataset.ix;
    wx.request({
      url: 'https://www.kehue.com/nuo/userStudents',
      data:{
        "studentId": sId,
        "relationship": parseInt(ix)+1
      },
      header: {
        ticket: ticket
      },
      method: 'POST',
      success: function (res) {
        console.log("绑定的结果");
        console.log(res);
        var index = e.target.dataset.index;
        var childs = that.data.childs;
        //移除列表中下标为index的项
        childs[index].bindState = false;
        var data = res.data;
        if (data.code == 200) {
          that.setData({
            childs: childs
          });
          wx.showToast({
            title: '成功',
            icon: 'success',
            duration: 2000
          })
        } else {
          console.log("获取到的ticket过期了");
          that.setData({
            message: "登录过期，请重新登录"
          });
        }
      }
    })
  },
  getSchools(sid, ticket) {
    var that = this;
    wx.request({
      url: 'https://www.kehue.com/nuo/schools?id=' + sid,
      header: {
        ticket: ticket
      },
      method: 'GET',
      success: function (res) {
        var childs = that.data.childs;
        //移除列表中下标为index的项
        var data = res.data;
        if (data.code == 200) {
          for (var i = 0; i < childs.length; i++) {
            if (childs[i].schoolId == sid) {
              childs[i].shcoolName = data.data[0].name;
            }
          }
          that.setData({
            childs: childs
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
  getClass(cid, ticket) {
    var that = this;
    wx.request({
      url: 'https://www.kehue.com/nuo/classes?id=' + cid,
      header: {
        ticket: ticket
      },
      method: 'GET',
      success: function (res) {
        var childs = that.data.childs;
        //移除列表中下标为index的项
        var data = res.data;
        if (data.code == 200) {
          for (var i = 0; i < childs.length; i++) {
            if (childs[i].classId == cid) {
              childs[i].className = data.data[0].grade + "年级" + data.data[0].number + "班";
            }
          }
          that.setData({
            childs: childs
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