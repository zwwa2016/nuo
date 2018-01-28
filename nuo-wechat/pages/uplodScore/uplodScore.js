// pages/uplodScore/uplodScore.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    imageList: [],
    uplodState: "show",
    typeList: [{ id: 1, value: "考试", checked: 'true'},{id:2,value:"作业"}],
    typeValue:1,
    sId:0,
    sBId:0,
    scoreProgress:[],
    num:0,
    pic:""
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    var sBId = options.sBId;
    var sId = options.sId;
    that.setData({
      sId: sId,
      sBId: sBId
    })
  },
  radioChange: function (e) {
    console.log('radio发生change事件，携带value值为：', e.detail.value)
    this.setData({
      typeValue: e.detail.value
    })
  },
  chooseImage: function () {
    var that = this
    var imageList = that.data.imageList;
    var length = imageList.length;
    wx.chooseImage({
      count: 9-length,
      success: function (res) {
        console.log(res)
        var list = res.tempFilePaths;
        for(var i=0;i<list.length;i++){
          imageList.push(list[i]);
        }
        length = imageList.length;
        var upload = "show";
        if(length>8){
          upload = "hide";
        } else {
          upload = "show";
        }
        that.setData({
          imageList: imageList,
          uplodState:upload
        })
      }
    })
  },
  previewImage: function (e) {
    var current = e.target.dataset.src

    wx.previewImage({
      current: current,
      urls: this.data.imageList
    })
  },
  sumbitScore:function(e){
    wx.showLoading({
      mask: true,
      title: '加载中',
    });
    var that = this;
    var typeValue = that.data.typeValue;
    var current = e.detail.value;
    var score = current.score;
    var imageList = that.data.imageList;
    console.log(current);
    var ticket = wx.getStorageSync('ticket');
    for(var i=0;i<imageList.length;i++){
      that.image(that,ticket,imageList[i]);
    }
    var intervalId = setInterval(function () {
      var num = that.data.num;
      if (num == imageList.length) {
        clearInterval(intervalId);
        wx.request({
          url: 'https://www.kehue.com/nuo/scores',
          header: {
            ticket: ticket
          },
          data: {
            "type": typeValue,
            "studentId": that.data.sId,
            "score": score,
            "pic": that.data.pic,
            "scoreBatchId": that.data.sBId
          },
          method: 'POST',
          success: function (res) {
            //移除列表中下标为index的项
            var data = res.data;
            console.log(data);
            if (data.code == 200) {
              wx.hideLoading();
              wx.navigateBack();
            } else {
              wx.hideLoading();
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
      }
    }, 300);
    
    
  },
  image:function(that,ticket,imagePath){
    const uploadTask = wx.uploadFile({
      url: 'https://www.kehue.com/nuo/files', //仅为示例，非真实的接口地址
      filePath: imagePath,
      header: {
        ticket: ticket
      },
      name: 'file',
      success: function (res) {
        var data = res.data
        var num = that.data.num;
        num++;
        data = JSON.parse(data);
        var pic = that.data.pic;
        if (pic == "") {
          pic += data.url;
        } else {
          pic += "," + data.url;
        }
        that.setData({
          pic: pic,
          num:num
        });
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