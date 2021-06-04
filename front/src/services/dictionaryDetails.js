import {request, METHOD} from '@/utils/request'
import {DICTIONARY_DETAILS} from './api'

/**
 * 获取字典明细信息，分页获取
 * @param page {{size: number, page: number}}
 * @returns {Promise<AxiosResponse<T>>}
 */
export async function list(page ) {
    return request(DICTIONARY_DETAILS, METHOD.GET, page||{"page": 1, "size": 10})
}

/**
 * 删除一个字典明细信息
 * @param id {string|number}
 * @returns {Promise<AxiosResponse<T>>}
 */
export async function deleteItem(id) {
    return request(DICTIONARY_DETAILS + '/' + id, METHOD.DELETE)
}

/**
 * 获取单个字典明细的详细信息
 * @param id
 * @returns {Promise<AxiosResponse<T>>}
 */
export async function getDetail(id) {
    return request(DICTIONARY_DETAILS + '/' + id, METHOD.GET)
}

/**
 * 修改字典明细信息
 * @param object {{
  "expression": "string",
  "id": 0,
  "name": "string"
}}
 * @returns {Promise<AxiosResponse<T>>}
 */
export async function update(object) {
    return request(DICTIONARY_DETAILS, METHOD.PUT, object)
}

/**
 * 添加字典明细信息
 * @param object {{
  "expression": "string",
  "name": "string"
}}
 * @returns {Promise<AxiosResponse<T>>}
 */
export async function add(object) {
    return request(DICTIONARY_DETAILS, METHOD.POST, object)
}
