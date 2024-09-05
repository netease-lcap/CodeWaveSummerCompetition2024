const { Solar, Lunar, LunarUtil, LunarYear } = require('lunar-javascript')
import dayjs from "dayjs"

export default (dateData) => {
  return Solar.fromDate(dayjs(dateData).toDate()).getLunar()
}

export {
  Lunar, LunarUtil, LunarYear
}

export function getNumberDate(dateStr) {
  const date = dateStr.replace(/\S/g, match => {
    switch (match) {
      case '初': return '';
      case '零': return '0';
      case '〇': return '0';
      case '正': return '1';
      case '一': return '1';
      case '二': return '2';
      case '三': return '3';
      case '四': return '4';
      case '五': return '5';
      case '六': return '6';
      case '七': return '7';
      case '八': return '8';
      case '九': return '9';
      case '十': return '1';
      case '廿': return '2';
      default:
        return ","
    }
  });
  return date
}

// console.log(getNumberDate("一九八六年四月廿一"))
