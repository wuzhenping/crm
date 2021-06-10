<template>
  <a-upload action="" :multiple="true" :customRequest="uploadFileMethod" @change="handleChange" :file-list="fileList"
    :showUploadList="showUploadList">
    <a-button>
      <a-icon type="upload" /> {{text}}
    </a-button>
  </a-upload>
</template>
<script>
import { uploadFile, getFileInfo } from "@/services/uploadFile.js";
export default {
  name: 'UploadFile',
  props: {
    text: {
      type: String,
      default: '点击上传'
    },
    showUploadList: {
      type: Boolean,
      default: true
    },
    fileid: String,
    url: String,
  },
  data () {
    return {
      fileList: [
        {
          uid: '-1',
          name: 'xxx.png',
          status: 'done',
          url: 'http://www.baidu.com/xxx.png',
        },
      ],
      fileID: ''
    };
  },
  watch: {
    'fileid': function () {
      return this.initData()
    },
  },
  created () {
    this.initData()
  },
  methods: {
    initData () {
      console.log('获取附件信息')
      const fileIDs = this.fileid // '41751215530463232'
      console.log('const fileIDs', fileIDs)
      if (fileIDs == '' || fileIDs == undefined || fileIDs == null) {
        this.fileList = []
        return
      }
      this.fileList = []
      let fileListArr = [];
      getFileInfo(fileIDs).then(res => {
        console.log(res.data)
        const fileInfo = res.data.data
        console.log(fileInfo)
        const baseImagUrl = process.env.VUE_APP_API_BASE_URL
        // let find = local.fileList.find(row => {
        //   return fileInfo.fileID === row.fileId
        // })
        // if (fileInfo && !find) {
        fileListArr.push({
          category: fileInfo.category,
          id: fileInfo.id,
          inputtime: fileInfo.inputtime,
          inputuser: fileInfo.inputuser,
          orifilename: fileInfo.orifilename,
          url: baseImagUrl + '/' + fileInfo.url
          // url: baseImagUrl + '/' + fileInfo.destFile + '?imageMogr2/thumbnail/360x360/format/webp/quality/100'
        })
        // }
        console.log(this.fileList)
        console.log(fileListArr)
      })

    },
    handleChange (info) {
      console.log(info)
      let fileList = [...info.fileList];

      // 1. 限制上传文件数量
      //    只显示两个最近上传的文件，旧的将被新的取代
      fileList = fileList.slice(-2);
      console.log(fileList)

      // 2. 从响应读取并显示文件链接
      // fileList = fileList.map(file => {
      //   if (file.response) {
      //     // 组件将显示文件。url链接
      //     file.url = file.response.url;
      //   }
      //   return file;
      // });

      this.fileList = fileList;
    },
    uploadFileMethod (param) {
      // var that = this
      const fileObject = param.file
      console.log(fileObject)
      const formData = new FormData()
      formData.append('file', fileObject)
      let data = {
        category: 'Image',
        // fileSequence: this.fileSequence || '',
        // fileType: this.fileType || '',
        // linkKey: this.linkKey || '',
      };

      // let url = that.url
      // if (that.url === undefined) {
      //   url = '/attachment/upload/file'
      // }
      uploadFile(formData, data).then(response => {
        console.log(response.data)
        if (response.data.code == "200") {
          let formObj = response.data.data
          console.log(formObj)
          // this.fileList = [formObj]
          // console.log('fileList')
          // console.log(that.fileList)
          this.$emit('listen2ChildEventAdd', response.data)
        }
      })
    }
  },
};
</script>