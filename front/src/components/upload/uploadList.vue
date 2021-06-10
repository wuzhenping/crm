<template>
  <div>
    <div class="upload_list">
      <div class="list_item" v-for="(item,index) in fileList" :key="item.id">
        <div class="file_name" @click="handlePreview(item)">{{item.orifilename}}</div>
        <div class="file_operation"><span @click="handlePreview(item)">查看</span><span
            @click="handlePreview(item)">下载</span><span @click="confirm(item,index)">删除</span></div>
      </div>
    </div>
  </div>
</template>
<script>
import { getFileInfo, downloadFile, deleteFile, previewFile } from "@/services/uploadFile.js";
export default {
  name: 'UploadList',
  props: {
    fileid: String,
    filelist: Array,
    showDelete: {
      type: Boolean,
      default: true
    },
  },
  data () {
    return {
      fileList: [
        {
          category: "file",
          id: "2106092182656",
          inputtime: 1623205772000,
          inputuser: 1,
          orifilename: "测试文档.doc",
          url: "D:/temp/crm/attach/file/2106092182656.doc"
        }
      ],
    };
  },
  watch: {
    'fileid': function () {
      return this.initData()
    }
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
      getFileInfo(fileIDs).then(res => {
        const fileInfo = res.data.data
        console.log(fileInfo)
        this.fileList = fileInfo
        console.log(this.fileList)
      })
    },
    handlePreview (item) {
      console.log(item)
      if (!/.(gif|jpg|jpeg|png|gif|jpg|png)$/.test(item.orifilename)) {
        console.log(item.orifilename)
        this.handlePreview2(item)
        return
      }
      // 预览图片
      let url = process.env.VUE_APP_API_BASE_URL + '/' + item.url
      // window.open(url, '_blank')
      previewFile(item.id).then(res => {
        if (res.status == 200) {
          console.log(res)
          console.log(url)
          window.open(url, '_blank')
        }
      })
    },
    handlePreview2 (fileObj) {
      downloadFile(fileObj.id)
    },
    confirm (fileObj, index) {
      let that = this
      let fileList_ = that.fileList
      this.$confirm({
        title: '删除附件',
        content: '确定删除 ' + fileObj.orifilename + ' ?',
        okText: '确认',
        cancelText: '取消',
        centered: true,
        onOk () {
          console.log(fileObj, index)
          deleteFile(fileObj.id).then(res => {
            console.log(res.data)
            if (res.status == 200) {
              fileList_.splice(index, 1)
              that.fileList = fileList_
            } else {
              console.log('失败')
            }
          })
        }
      })
    }
  },
};
</script>
<style lang="less" scoped>
.upload_list {
  .list_item {
    display: flex;
    justify-content: space-between;
    .file_name {
      flex: 1;
      text-overflow: ellipsis;
      overflow: hidden;
      white-space: nowrap;
      cursor: pointer;
    }
    .file_name:hover {
      color: #40a9ff;
    }
    .file_operation {
      span {
        cursor: pointer;
        margin-left: 8px;
      }
      span:hover {
        color: #40a9ff;
      }
    }
  }
}
</style>