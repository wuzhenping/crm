import axios from 'axios'
import {
  UPLOADFILE,
  GETFILEINFO,
  DOWNLOADFILE,
  DELETEFILE
} from '@/services/api'
import {
  request,
  METHOD,
} from '@/utils/request'


/**
 * 文件上传
 */
export async function uploadFile(formData) {
  const config = {}
  config.headers = {}
  config.headers['Content-Type'] = 'multipart/form-data'
  // formData.append('category', data.category)
  // return request(UPLOADFILE, METHOD.POST, data)
  // if (checkAuthorization()) {
  //   config.headers['Authorization'] = setAuthorization({token:'testToken'})
  // }
  const baseURL = UPLOADFILE
  var url = baseURL //'/attachment/uploads/file'
  return axios.post(url, formData, config)
}
/**
 * 获取附件信息
 */
export async function getFileInfo(fileId) {
  return request(GETFILEINFO + '/' + fileId, METHOD.GET)
}
/**
 * 查看文件
 */
export async function getFindFileUrl(fileId) {
  return request(GETFILEINFO, METHOD.POST, {
    fileId
  })
}
/**
 * 下载附件
 */
export async function downloadFile(fileId) {
  window.open(DOWNLOADFILE + '/' + fileId, '_blank')
  // return request(DOWNLOADFILE + '/' + fileId, METHOD.GET)
}
// 预览图片
export async function previewFile(fileId) {
  return request(DOWNLOADFILE + '/' + fileId, METHOD.GET, {
    responseType: 'blob'
  })
}
/**
 * 删除文件
 */
export async function deleteFile(fileId) {
  return request(DELETEFILE + '/' + fileId, METHOD.DELETE)
}