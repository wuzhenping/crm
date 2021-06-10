<template>
  <div>
    <div class="upload_file">
      <UploadFile @listen2ChildEventAdd="handleUpload" @listen2ChildEventDel="handleRemove" :fileid="fileid2"
        :text="text2" />
    </div>
    <div class="upload_list">
      <UploadList :fileid="fileid2" />
    </div>
  </div>
</template>
<script>
import UploadFile from '@/components/upload/uploadFile.vue'
import UploadList from '@/components/upload/uploadList.vue'
import { deleteFile } from "@/services/uploadFile.js";
export default {
  name: 'UploadComponent',
  components: {
    UploadFile,
    UploadList
  },
  props: {
    text2: {
      type: String,
      default: '点击上传'
    },
    fileid2: String,
  },
  data () {
    return {
      fileID: ''
    };
  },
  methods: {
    handleUpload (res) {
      this.$emit('listen2ChildEventAdd', res)
    },
    handleRemove (file, fileList) {
      console.log('fileList', fileList)
      if (file) {
        deleteFile(file.fileID).then(res => {
          console.log('res', res)
          this.$notify({
            title: '提示',
            message: '文件删除成功',
            type: 'success'
          });
          this.$emit('listen2ChildEventDel', file)
        }).catch((error) => { console.log(error) })
      }
    },
    handleChange () {
      console.log('info')
    },
  },
};
</script>
