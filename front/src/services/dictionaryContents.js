import {request, METHOD} from '@/utils/request'
import {DICTIONARY_CONTENTS} from './api'

/**
 * 获取字典列表信息，分页获取
 * @param page {{size: number, page: number}}
 * @returns {Promise<AxiosResponse<T>>}
 */
export async function list(page ) {
    return request(DICTIONARY_CONTENTS, METHOD.GET, page||{"page": 1, "size": 10})
}

/**
 * 删除一个字典列表信息
 * @param id {string|number}
 * @returns {Promise<AxiosResponse<T>>}
 */
export async function deleteItem(id) {
    return request(DICTIONARY_CONTENTS + '/' + id, METHOD.DELETE)
}

/**
 * 获取单个字典列表的详细信息
 * @param id
 * @returns {Promise<AxiosResponse<T>>}
 */
export async function getDetail(id) {
    return request(DICTIONARY_CONTENTS + '/' + id, METHOD.GET)
}

/**
 * 修改字典列表信息
 * @returns {Promise<AxiosResponse<T>>}
 */
export async function update(object) {
    return request(DICTIONARY_CONTENTS, METHOD.PUT, object)
}

/**
 * 添加字典列表信息
 * @returns {Promise<AxiosResponse<T>>}
 */
export async function add(object) {
    return request(DICTIONARY_CONTENTS, METHOD.POST, object)
}
