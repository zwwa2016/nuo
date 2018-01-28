// pages/children/children.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    load:false,//load方法是否已经执行
    childs:[],
    errorShow:'hide',
    message:"",
    work: [
      {
        id: 'transcript',
        name: '成绩单'
      }, {
        id: 'homework',
        name: '作业'
      }
    ],
    relation: ["","自己", "父亲", "母亲", "爷爷", "奶奶", "姑姑", "姑父", "姨", "姨父", "创建者"]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    that.setData({
      load: true
    });
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
        var code = res.code;
        if (res.code) {
          wx.getUserInfo({
            success: res => {
              wx.request({
                url: 'https://www.kehue.com/nuo/wechatLogin',//自己的服务接口地址
                data: {
                  encryptedData: res.encryptedData,
                  iv: res.iv,
                  code: code
                },
                success: function (data) {
                  console.log("登录成功");
                  //4.解密成功后 获取自己服务器返回的结果
                  console.log(data)
                  if (data.data.code == 200) {
                    //console.log(userInfo_)
                    wx.setStorageSync('ticket', data.data.ticket);
                    wx.setStorageSync('role', data.data.loginUser.role);
                    
                    that.requestData();
                  } else {
                    console.log('解密失败')
                  }
                },
                fail: function () {
                  console.log('系统错误')
                }
              })
            }
          })
        } else {
          console.log("login fail, " + res.errMsg);
        }
      }
    })

    console.log("onLoad方法");
    
  },

  addChildrenTap: function () {
    console.log("跳转到新增孩子页面");
    wx.navigateTo({
      url: '../findChild/findChild'
    })
  },
  requestData: function () {
    var that = this;
    var ticket = wx.getStorageSync('ticket');
    console.log("我的孩子页面ticket：" + ticket);
    wx.request({
      url: 'https://www.kehue.com/nuo/students?page=1&pageLength=10',
      header: {
        ticket: ticket
      },
      method: 'GET',
      success: function (res) {
        console.log("获取我的孩子");
        console.log(res);
        var data = res.data;
        if (data.code == 200) {
          var childList = data.data;
          if (childList.length == 0) {
            that.setData({
              errorShow: 'show',
              message: "您还没有绑定孩子，请先绑定。"
            });
          } else {
            for (var i = 0; i < childList.length; i++) {
              var child = childList[i];
              child.birthday = that.formatDate(child.birthday);
              child.works = that.data.work;
              that.getSchools(child.schoolId, ticket);
              that.getClass(child.classId, ticket);
            }
            that.setData({
              errorShow:'hide',
              childs: childList
            });
            console.log(that.data.childs);
          }
          
          
        } else {
          console.log("获取到的ticket过期了");
          that.setData({
            errorShow: 'show',
            message: "登录过期，请重新登录"
          });
        }
      }
    })
  },
  formatDate: function (date) {
    return date.substring(0, 10);
  },
  unBindChild: function (e) {
    const dataset = e.currentTarget.dataset;
    console.log(e)
    console.log("孩子id：" + dataset.id);
    var that = this;
    var ticket = wx.getStorageSync('ticket');
    var reId = dataset.id;
    wx.request({
      url: 'https://www.kehue.com/nuo/userStudents?id='+reId,
      header: {
        ticket: ticket
      },
      method: 'DELETE',
      success: function (res) {
        console.log("解除绑定的结果");
        console.log(res);
        var index = e.target.dataset.index;
        var childs = that.data.childs;
        childs.splice(index, 1);
        //移除列表中下标为index的项
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
  getSchools(sid,ticket){
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
          for(var i=0;i<childs.length;i++){
            if (childs[i].schoolId==sid){
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
              childs[i].className = data.data[0].grade + "年级" + data.data[0].number+"班";
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
    var that = this;
    var load = that.data.load;
    console.log(load);
    if(!load){
      console.log("onShow方法");
      that.requestData();
    }
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    this.setData({
      load: false
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