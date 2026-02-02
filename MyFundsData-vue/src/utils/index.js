// 前端工具类库
// 基于 lodash 扩展
import _ from 'lodash';

/**
 * 日期时间工具类
 */
export const dateUtil = {
  /**
   * 格式化日期时间
   * @param {Date|string|number} date - 日期对象或时间戳
   * @param {string} format - 格式化字符串，例如：YYYY-MM-DD HH:mm:ss
   * @returns {string} 格式化后的日期时间字符串
   */
  format(date, format = 'YYYY-MM-DD HH:mm:ss') {
    if (!date) return '';
    
    const d = new Date(date);
    if (isNaN(d.getTime())) return '';
    
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    const hours = String(d.getHours()).padStart(2, '0');
    const minutes = String(d.getMinutes()).padStart(2, '0');
    const seconds = String(d.getSeconds()).padStart(2, '0');
    
    return format
      .replace('YYYY', year)
      .replace('MM', month)
      .replace('DD', day)
      .replace('HH', hours)
      .replace('mm', minutes)
      .replace('ss', seconds);
  },
  
  /**
   * 获取当前日期时间
   * @param {string} format - 格式化字符串
   * @returns {string} 当前日期时间字符串
   */
  now(format = 'YYYY-MM-DD HH:mm:ss') {
    return this.format(new Date(), format);
  },
  
  /**
   * 获取当前日期
   * @returns {string} 当前日期字符串，格式：YYYY-MM-DD
   */
  today() {
    return this.now('YYYY-MM-DD');
  },
  
  /**
   * 计算两个日期之间的天数差
   * @param {Date|string|number} startDate - 开始日期
   * @param {Date|string|number} endDate - 结束日期
   * @returns {number} 天数差
   */
  betweenDays(startDate, endDate) {
    const start = new Date(startDate);
    const end = new Date(endDate);
    const diffTime = Math.abs(end - start);
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  }
};

/**
 * 字符串工具类
 */
export const stringUtil = {
  /**
   * 检查字符串是否为空
   * @param {string} str - 字符串
   * @returns {boolean} true 为空，false 不为空
   */
  isEmpty(str) {
    return _.isEmpty(str);
  },
  
  /**
   * 检查字符串是否不为空
   * @param {string} str - 字符串
   * @returns {boolean} true 不为空，false 为空
   */
  isNotEmpty(str) {
    return !_.isEmpty(str);
  },
  
  /**
   * 检查字符串是否为空白
   * @param {string} str - 字符串
   * @returns {boolean} true 为空白，false 不为空白
   */
  isBlank(str) {
    return _.isString(str) ? str.trim() === '' : _.isEmpty(str);
  },
  
  /**
   * 检查字符串是否不为空白
   * @param {string} str - 字符串
   * @returns {boolean} true 不为空白，false 为空白
   */
  isNotBlank(str) {
    return !this.isBlank(str);
  },
  
  /**
   * 截断字符串
   * @param {string} str - 字符串
   * @param {number} length - 截断长度
   * @param {string} suffix - 后缀
   * @returns {string} 截断后的字符串
   */
  truncate(str, length, suffix = '...') {
    if (!str) return '';
    if (str.length <= length) return str;
    return str.substring(0, length) + suffix;
  },
  
  /**
   * 首字母大写
   * @param {string} str - 字符串
   * @returns {string} 首字母大写后的字符串
   */
  capitalize(str) {
    return _.upperFirst(str);
  },
  
  /**
   * 首字母小写
   * @param {string} str - 字符串
   * @returns {string} 首字母小写后的字符串
   */
  uncapitalize(str) {
    return _.lowerFirst(str);
  },
  
  /**
   * 字符串转下划线格式
   * @param {string} str - 字符串
   * @returns {string} 下划线格式字符串
   */
  toUnderlineCase(str) {
    return _.toLower(_.kebabCase(str)).replace(/-/g, '_');
  },
  
  /**
   * 字符串转驼峰格式
   * @param {string} str - 字符串
   * @returns {string} 驼峰格式字符串
   */
  toCamelCase(str) {
    return _.camelCase(str);
  },
  
  /**
   * 移除字符串中的空格
   * @param {string} str - 字符串
   * @returns {string} 移除空格后的字符串
   */
  removeWhitespace(str) {
    if (!str) return str;
    return str.replace(/\s+/g, '');
  },
  
  /**
   * 格式化字符串
   * @param {string} format - 格式字符串
   * @param {any[]} args - 参数
   * @returns {string} 格式化后的字符串
   */
  format(format, ...args) {
    return format.replace(/\{(\d+)\}/g, (match, index) => {
      return typeof args[index] !== 'undefined' ? args[index] : match;
    });
  },
  
  /**
   * 连接字符串
   * @param {string} separator - 分隔符
   * @param {string[]} strs - 字符串数组
   * @returns {string} 连接后的字符串
   */
  join(separator, ...strs) {
    return strs.join(separator);
  },
  
  /**
   * 反转字符串
   * @param {string} str - 字符串
   * @returns {string} 反转后的字符串
   */
  reverse(str) {
    return str.split('').reverse().join('');
  }
};

/**
 * 数字工具类
 */
export const numberUtil = {
  /**
   * 格式化数字
   * @param {number} num - 数字
   * @param {number} decimals - 小数位数
   * @returns {string} 格式化后的数字字符串
   */
  format(num, decimals = 2) {
    if (num === null || num === undefined) return '0';
    return Number(num).toFixed(decimals);
  },
  
  /**
   * 格式化百分比
   * @param {number} num - 数字
   * @param {number} decimals - 小数位数
   * @returns {string} 格式化后的百分比字符串
   */
  formatPercent(num, decimals = 2) {
    if (num === null || num === undefined) return '0%';
    return `${(Number(num) * 100).toFixed(decimals)}%`;
  },
  
  /**
   * 格式化金额
   * @param {number} amount - 金额
   * @param {number} decimals - 小数位数
   * @returns {string} 格式化后的金额字符串
   */
  formatMoney(amount, decimals = 2) {
    if (amount === null || amount === undefined) return '0.00';
    return Number(amount).toFixed(decimals);
  },
  
  /**
   * 生成随机数
   * @param {number} min - 最小值
   * @param {number} max - 最大值
   * @returns {number} 随机数
   */
  random(min, max) {
    return _.random(min, max);
  },
  
  /**
   * 检查数字是否在指定范围内
   * @param {number} num - 数字
   * @param {number} min - 最小值
   * @param {number} max - 最大值
   * @returns {boolean} true 在范围内，false 不在范围内
   */
  inRange(num, min, max) {
    return _.inRange(num, min, max);
  }
};

/**
 * 数组工具类
 */
export const arrayUtil = {
  /**
   * 检查数组是否为空
   * @param {any[]} arr - 数组
   * @returns {boolean} true 为空，false 不为空
   */
  isEmpty(arr) {
    return _.isEmpty(arr);
  },
  
  /**
   * 检查数组是否不为空
   * @param {any[]} arr - 数组
   * @returns {boolean} true 不为空，false 为空
   */
  isNotEmpty(arr) {
    return !_.isEmpty(arr);
  },
  
  /**
   * 数组去重
   * @param {any[]} arr - 数组
   * @returns {any[]} 去重后的数组
   */
  uniq(arr) {
    return _.uniq(arr);
  },
  
  /**
   * 数组分组
   * @param {any[]} arr - 数组
   * @param {string|function} iteratee - 分组依据
   * @returns {Object} 分组后的对象
   */
  groupBy(arr, iteratee) {
    return _.groupBy(arr, iteratee);
  },
  
  /**
   * 数组排序
   * @param {any[]} arr - 数组
   * @param {string|function} iteratee - 排序依据
   * @param {string} order - 排序顺序，asc 升序，desc 降序
   * @returns {any[]} 排序后的数组
   */
  sortBy(arr, iteratee, order = 'asc') {
    const sorted = _.sortBy(arr, iteratee);
    return order === 'desc' ? sorted.reverse() : sorted;
  }
};

/**
 * 对象工具类
 */
export const objectUtil = {
  /**
   * 检查对象是否为空
   * @param {Object} obj - 对象
   * @returns {boolean} true 为空，false 不为空
   */
  isEmpty(obj) {
    return _.isEmpty(obj);
  },
  
  /**
   * 检查对象是否不为空
   * @param {Object} obj - 对象
   * @returns {boolean} true 不为空，false 为空
   */
  isNotEmpty(obj) {
    return !_.isEmpty(obj);
  },
  
  /**
   * 深拷贝对象
   * @param {Object} obj - 对象
   * @returns {Object} 拷贝后的对象
   */
  cloneDeep(obj) {
    return _.cloneDeep(obj);
  },
  
  /**
   * 合并对象
   * @param {Object} target - 目标对象
   * @param {Object[]} sources - 源对象数组
   * @returns {Object} 合并后的对象
   */
  merge(target, ...sources) {
    return _.merge(target, ...sources);
  },
  
  /**
   * 获取对象属性
   * @param {Object} obj - 对象
   * @param {string} path - 属性路径
   * @param {any} defaultValue - 默认值
   * @returns {any} 属性值
   */
  get(obj, path, defaultValue) {
    return _.get(obj, path, defaultValue);
  },
  
  /**
   * 设置对象属性
   * @param {Object} obj - 对象
   * @param {string} path - 属性路径
   * @param {any} value - 属性值
   * @returns {Object} 对象
   */
  set(obj, path, value) {
    return _.set(obj, path, value);
  }
};

/**
 * 通用工具类
 */
export const commonUtil = {
  /**
   * 防抖函数
   * @param {function} func - 函数
   * @param {number} wait - 等待时间
   * @returns {function} 防抖后的函数
   */
  debounce(func, wait) {
    return _.debounce(func, wait);
  },
  
  /**
   * 节流函数
   * @param {function} func - 函数
   * @param {number} wait - 等待时间
   * @returns {function} 节流后的函数
   */
  throttle(func, wait) {
    return _.throttle(func, wait);
  },
  
  /**
   * 生成唯一ID
   * @returns {string} 唯一ID
   */
  uniqueId() {
    return _.uniqueId();
  },
  
  /**
   * 检查类型
   * @param {any} value - 值
   * @param {string} type - 类型
   * @returns {boolean} true 是指定类型，false 不是指定类型
   */
  isType(value, type) {
    return Object.prototype.toString.call(value).toLowerCase() === `[object ${type.toLowerCase()}]`;
  }
};

// 导出所有工具类
export default {
  dateUtil,
  stringUtil,
  numberUtil,
  arrayUtil,
  objectUtil,
  commonUtil
};
