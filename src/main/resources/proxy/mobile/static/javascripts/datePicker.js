!function(b){b.datePicker=function(e){function f(b,e){var f=[31,28,31,30,31,30,31,31,30,31,30,31];return e%4!=0||e%100==0&&e%400!=0||(f[1]=29),f[b]}if(e=b.extend({currentDate:"",title:"选择日期",serverDate:"",expire:!1,endDate:"",onSelect:function(b){}},e),e.serverDate.length<=0){var c=new Date;e.serverDate=c.getFullYear()+"-"+(c.getMonth()+1)+"-"+c.getDate()}var a='<div id="dateContainer" class="sui-popup-container"><div class="sui-popup-mask"></div><div class="sui-popup-modal"><header><div class="left-btn icon-back"></div><h1>'+e.title+'</h1></header><div id="datePicker" class="sui-border-b"><div class="date-picker-year"><a class="date-picker-prev-month"></a><h3>2016年12月</h3><a class="date-picker-next-month"></a></div><table border="0" cellspacing="0" cellpadding="0"><thead><tr><th class="day-rest">日</th><th>一</th><th>二</th><th>三</th><th>四</th><th>五</th><th class="day-rest">六</th></tr><tr><td colspan="7" class="distance-head"></td></tr></thead><tbody>{{for(var i=0;i<6;i++){}}<tr>{{for(var y=0;y<7;y++){}}<td><span></span></td>{{}}}</tr>{{}}}</tbody><tfoot><tr><td colspan="7" class="distance-foot"></td></tr></tfoot></table></div><div class="btn-bar"><div class="wrap"><button class="btn clear-select-time">取消选择</button></div></div></div></div>';b(doT.template(a)(e)).appendTo(document.body),b("#dateContainer").popup("push",null,function(f){"clear"==f&&(f=null),b.isFunction(e.onSelect)&&e.onSelect(f)}),b("#dateContainer .left-btn").on("click",function(){b("#dateContainer").closePopup()}),b("#dateContainer .clear-select-time").on("click",function(){b("#dateContainer").setPopupData("clear").closePopup()});var t=function(c,a,t){b("#datePicker tbody td").html("").attr("class","").data("day","").off("click");var r=new Date(t,a,1),d=r.getDay(),n=d+f(r.getMonth(),r.getFullYear);b("#datePicker h3").text(r.getFullYear()+"年"+(r.getMonth()+1)+"月");for(var s=1,l=Date.parse(e.currentDate.replace(/-/g,"/")),o=Date.parse(e.serverDate.replace(/-/g,"/")),i=b("#datePicker tbody td"),u=d;u<n;u++){var p=t+"-"+(a+1)+"-"+s,h=Date.parse(p.replace(/-/g,"/")),D=calendar.solar2lunar(t,a+1,s);if(i.eq(u).data("day",p).html("<p>"+s+"<span>"+D.IDayCn+"</span></p>"),e.expire||e.endDate.length>0){var v=Date.parse(e.endDate.replace(/-/g,"/"));o>h?i.eq(u).addClass("disabled"):v<h?i.eq(u).addClass("disabled"):i.eq(u).addClass("enabled")}else i.eq(u).addClass("enabled");c>0&&l==h&&i.eq(u).addClass("active"),o==h&&i.eq(u).addClass("current"),s++}b("#datePicker .enabled").on("click",function(){var e=b(this).data("day"),f=Date.parse(e.replace(/-/g,"/")),c=e.split("-"),a=new Date(f),t=["日","一","二","三","四","五","六"],r=new Date,d=r.getFullYear()+"-"+(r.getMonth()+1)+"-"+r.getDate(),n={str:e,year:c[0],month:c[1],date:c[2],timestamp:f,object:a,week:"星期"+t[a.getDay()],isToday:e==d};b("#dateContainer").setPopupData(n).closePopup()})},r=e.currentDate;if(r.length>0)var d=r.split("-"),n=d[0],s=d[1]-1,l=d[2];else var d=new Date,n=d.getFullYear(),s=d.getMonth(),l=0;b("#datePicker .date-picker-prev-month").on("click",function(){s-1<0?(s=11,n--):s--,t(l,s,n)}),b("#datePicker .date-picker-next-month").on("click",function(){s+1>11?(s=0,n++):s++,t(l,s,n)}),t(l,s,n)}}(Zepto);var calendar={lunarInfo:[19416,19168,42352,21717,53856,55632,91476,22176,39632,21970,19168,42422,42192,53840,119381,46400,54944,44450,38320,84343,18800,42160,46261,27216,27968,109396,11104,38256,21234,18800,25958,54432,59984,28309,23248,11104,100067,37600,116951,51536,54432,120998,46416,22176,107956,9680,37584,53938,43344,46423,27808,46416,86869,19872,42416,83315,21168,43432,59728,27296,44710,43856,19296,43748,42352,21088,62051,55632,23383,22176,38608,19925,19152,42192,54484,53840,54616,46400,46752,103846,38320,18864,43380,42160,45690,27216,27968,44870,43872,38256,19189,18800,25776,29859,59984,27480,21952,43872,38613,37600,51552,55636,54432,55888,30034,22176,43959,9680,37584,51893,43344,46240,47780,44368,21977,19360,42416,86390,21168,43312,31060,27296,44368,23378,19296,42726,42208,53856,60005,54576,23200,30371,38608,19195,19152,42192,118966,53840,54560,56645,46496,22224,21938,18864,42359,42160,43600,111189,27936,44448,84835,37744,18936,18800,25776,92326,59984,27424,108228,43744,41696,53987,51552,54615,54432,55888,23893,22176,42704,21972,21200,43448,43344,46240,46758,44368,21920,43940,42416,21168,45683,26928,29495,27296,44368,84821,19296,42352,21732,53600,59752,54560,55968,92838,22224,19168,43476,41680,53584,62034,54560],solarMonth:[31,28,31,30,31,30,31,31,30,31,30,31],Gan:["甲","乙","丙","丁","戊","己","庚","辛","壬","癸"],Zhi:["子","丑","寅","卯","辰","巳","午","未","申","酉","戌","亥"],Animals:["鼠","牛","虎","兔","龙","蛇","马","羊","猴","鸡","狗","猪"],solarTerm:["小寒","大寒","立春","雨水","惊蛰","春分","清明","谷雨","立夏","小满","芒种","夏至","小暑","大暑","立秋","处暑","白露","秋分","寒露","霜降","立冬","小雪","大雪","冬至"],sTermInfo:["9778397bd097c36b0b6fc9274c91aa","97b6b97bd19801ec9210c965cc920e","97bcf97c3598082c95f8c965cc920f","97bd0b06bdb0722c965ce1cfcc920f","b027097bd097c36b0b6fc9274c91aa","97b6b97bd19801ec9210c965cc920e","97bcf97c359801ec95f8c965cc920f","97bd0b06bdb0722c965ce1cfcc920f","b027097bd097c36b0b6fc9274c91aa","97b6b97bd19801ec9210c965cc920e","97bcf97c359801ec95f8c965cc920f","97bd0b06bdb0722c965ce1cfcc920f","b027097bd097c36b0b6fc9274c91aa","9778397bd19801ec9210c965cc920e","97b6b97bd19801ec95f8c965cc920f","97bd09801d98082c95f8e1cfcc920f","97bd097bd097c36b0b6fc9210c8dc2","9778397bd197c36c9210c9274c91aa","97b6b97bd19801ec95f8c965cc920e","97bd09801d98082c95f8e1cfcc920f","97bd097bd097c36b0b6fc9210c8dc2","9778397bd097c36c9210c9274c91aa","97b6b97bd19801ec95f8c965cc920e","97bcf97c3598082c95f8e1cfcc920f","97bd097bd097c36b0b6fc9210c8dc2","9778397bd097c36c9210c9274c91aa","97b6b97bd19801ec9210c965cc920e","97bcf97c3598082c95f8c965cc920f","97bd097bd097c35b0b6fc920fb0722","9778397bd097c36b0b6fc9274c91aa","97b6b97bd19801ec9210c965cc920e","97bcf97c3598082c95f8c965cc920f","97bd097bd097c35b0b6fc920fb0722","9778397bd097c36b0b6fc9274c91aa","97b6b97bd19801ec9210c965cc920e","97bcf97c359801ec95f8c965cc920f","97bd097bd097c35b0b6fc920fb0722","9778397bd097c36b0b6fc9274c91aa","97b6b97bd19801ec9210c965cc920e","97bcf97c359801ec95f8c965cc920f","97bd097bd097c35b0b6fc920fb0722","9778397bd097c36b0b6fc9274c91aa","97b6b97bd19801ec9210c965cc920e","97bcf97c359801ec95f8c965cc920f","97bd097bd07f595b0b6fc920fb0722","9778397bd097c36b0b6fc9210c8dc2","9778397bd19801ec9210c9274c920e","97b6b97bd19801ec95f8c965cc920f","97bd07f5307f595b0b0bc920fb0722","7f0e397bd097c36b0b6fc9210c8dc2","9778397bd097c36c9210c9274c920e","97b6b97bd19801ec95f8c965cc920f","97bd07f5307f595b0b0bc920fb0722","7f0e397bd097c36b0b6fc9210c8dc2","9778397bd097c36c9210c9274c91aa","97b6b97bd19801ec9210c965cc920e","97bd07f1487f595b0b0bc920fb0722","7f0e397bd097c36b0b6fc9210c8dc2","9778397bd097c36b0b6fc9274c91aa","97b6b97bd19801ec9210c965cc920e","97bcf7f1487f595b0b0bb0b6fb0722","7f0e397bd097c35b0b6fc920fb0722","9778397bd097c36b0b6fc9274c91aa","97b6b97bd19801ec9210c965cc920e","97bcf7f1487f595b0b0bb0b6fb0722","7f0e397bd097c35b0b6fc920fb0722","9778397bd097c36b0b6fc9274c91aa","97b6b97bd19801ec9210c965cc920e","97bcf7f1487f531b0b0bb0b6fb0722","7f0e397bd097c35b0b6fc920fb0722","9778397bd097c36b0b6fc9274c91aa","97b6b97bd19801ec9210c965cc920e","97bcf7f1487f531b0b0bb0b6fb0722","7f0e397bd07f595b0b6fc920fb0722","9778397bd097c36b0b6fc9274c91aa","97b6b97bd19801ec9210c9274c920e","97bcf7f0e47f531b0b0bb0b6fb0722","7f0e397bd07f595b0b0bc920fb0722","9778397bd097c36b0b6fc9210c91aa","97b6b97bd197c36c9210c9274c920e","97bcf7f0e47f531b0b0bb0b6fb0722","7f0e397bd07f595b0b0bc920fb0722","9778397bd097c36b0b6fc9210c8dc2","9778397bd097c36c9210c9274c920e","97b6b7f0e47f531b0723b0b6fb0722","7f0e37f5307f595b0b0bc920fb0722","7f0e397bd097c36b0b6fc9210c8dc2","9778397bd097c36b0b70c9274c91aa","97b6b7f0e47f531b0723b0b6fb0721","7f0e37f1487f595b0b0bb0b6fb0722","7f0e397bd097c35b0b6fc9210c8dc2","9778397bd097c36b0b6fc9274c91aa","97b6b7f0e47f531b0723b0b6fb0721","7f0e27f1487f595b0b0bb0b6fb0722","7f0e397bd097c35b0b6fc920fb0722","9778397bd097c36b0b6fc9274c91aa","97b6b7f0e47f531b0723b0b6fb0721","7f0e27f1487f531b0b0bb0b6fb0722","7f0e397bd097c35b0b6fc920fb0722","9778397bd097c36b0b6fc9274c91aa","97b6b7f0e47f531b0723b0b6fb0721","7f0e27f1487f531b0b0bb0b6fb0722","7f0e397bd097c35b0b6fc920fb0722","9778397bd097c36b0b6fc9274c91aa","97b6b7f0e47f531b0723b0b6fb0721","7f0e27f1487f531b0b0bb0b6fb0722","7f0e397bd07f595b0b0bc920fb0722","9778397bd097c36b0b6fc9274c91aa","97b6b7f0e47f531b0723b0787b0721","7f0e27f0e47f531b0b0bb0b6fb0722","7f0e397bd07f595b0b0bc920fb0722","9778397bd097c36b0b6fc9210c91aa","97b6b7f0e47f149b0723b0787b0721","7f0e27f0e47f531b0723b0b6fb0722","7f0e397bd07f595b0b0bc920fb0722","9778397bd097c36b0b6fc9210c8dc2","977837f0e37f149b0723b0787b0721","7f07e7f0e47f531b0723b0b6fb0722","7f0e37f5307f595b0b0bc920fb0722","7f0e397bd097c35b0b6fc9210c8dc2","977837f0e37f14998082b0787b0721","7f07e7f0e47f531b0723b0b6fb0721","7f0e37f1487f595b0b0bb0b6fb0722","7f0e397bd097c35b0b6fc9210c8dc2","977837f0e37f14998082b0787b06bd","7f07e7f0e47f531b0723b0b6fb0721","7f0e27f1487f531b0b0bb0b6fb0722","7f0e397bd097c35b0b6fc920fb0722","977837f0e37f14998082b0787b06bd","7f07e7f0e47f531b0723b0b6fb0721","7f0e27f1487f531b0b0bb0b6fb0722","7f0e397bd097c35b0b6fc920fb0722","977837f0e37f14998082b0787b06bd","7f07e7f0e47f531b0723b0b6fb0721","7f0e27f1487f531b0b0bb0b6fb0722","7f0e397bd07f595b0b0bc920fb0722","977837f0e37f14998082b0787b06bd","7f07e7f0e47f531b0723b0b6fb0721","7f0e27f1487f531b0b0bb0b6fb0722","7f0e397bd07f595b0b0bc920fb0722","977837f0e37f14998082b0787b06bd","7f07e7f0e47f149b0723b0787b0721","7f0e27f0e47f531b0b0bb0b6fb0722","7f0e397bd07f595b0b0bc920fb0722","977837f0e37f14998082b0723b06bd","7f07e7f0e37f149b0723b0787b0721","7f0e27f0e47f531b0723b0b6fb0722","7f0e397bd07f595b0b0bc920fb0722","977837f0e37f14898082b0723b02d5","7ec967f0e37f14998082b0787b0721","7f07e7f0e47f531b0723b0b6fb0722","7f0e37f1487f595b0b0bb0b6fb0722","7f0e37f0e37f14898082b0723b02d5","7ec967f0e37f14998082b0787b0721","7f07e7f0e47f531b0723b0b6fb0722","7f0e37f1487f531b0b0bb0b6fb0722","7f0e37f0e37f14898082b0723b02d5","7ec967f0e37f14998082b0787b06bd","7f07e7f0e47f531b0723b0b6fb0721","7f0e37f1487f531b0b0bb0b6fb0722","7f0e37f0e37f14898082b072297c35","7ec967f0e37f14998082b0787b06bd","7f07e7f0e47f531b0723b0b6fb0721","7f0e27f1487f531b0b0bb0b6fb0722","7f0e37f0e37f14898082b072297c35","7ec967f0e37f14998082b0787b06bd","7f07e7f0e47f531b0723b0b6fb0721","7f0e27f1487f531b0b0bb0b6fb0722","7f0e37f0e366aa89801eb072297c35","7ec967f0e37f14998082b0787b06bd","7f07e7f0e47f149b0723b0787b0721","7f0e27f1487f531b0b0bb0b6fb0722","7f0e37f0e366aa89801eb072297c35","7ec967f0e37f14998082b0723b06bd","7f07e7f0e47f149b0723b0787b0721","7f0e27f0e47f531b0723b0b6fb0722","7f0e37f0e366aa89801eb072297c35","7ec967f0e37f14998082b0723b06bd","7f07e7f0e37f14998083b0787b0721","7f0e27f0e47f531b0723b0b6fb0722","7f0e37f0e366aa89801eb072297c35","7ec967f0e37f14898082b0723b02d5","7f07e7f0e37f14998082b0787b0721","7f07e7f0e47f531b0723b0b6fb0722","7f0e36665b66aa89801e9808297c35","665f67f0e37f14898082b0723b02d5","7ec967f0e37f14998082b0787b0721","7f07e7f0e47f531b0723b0b6fb0722","7f0e36665b66a449801e9808297c35","665f67f0e37f14898082b0723b02d5","7ec967f0e37f14998082b0787b06bd","7f07e7f0e47f531b0723b0b6fb0721","7f0e36665b66a449801e9808297c35","665f67f0e37f14898082b072297c35","7ec967f0e37f14998082b0787b06bd","7f07e7f0e47f531b0723b0b6fb0721","7f0e26665b66a449801e9808297c35","665f67f0e37f1489801eb072297c35","7ec967f0e37f14998082b0787b06bd","7f07e7f0e47f531b0723b0b6fb0721","7f0e27f1487f531b0b0bb0b6fb0722"],nStr1:["日","一","二","三","四","五","六","七","八","九","十"],nStr2:["初","十","廿","卅"],nStr3:["正","二","三","四","五","六","七","八","九","十","冬","腊"],lYearDays:function(b){var e,f=348;for(e=32768;e>8;e>>=1)f+=calendar.lunarInfo[b-1900]&e?1:0;return f+calendar.leapDays(b)},leapMonth:function(b){return 15&calendar.lunarInfo[b-1900]},leapDays:function(b){return calendar.leapMonth(b)?65536&calendar.lunarInfo[b-1900]?30:29:0},monthDays:function(b,e){return e>12||e<1?-1:calendar.lunarInfo[b-1900]&65536>>e?30:29},solarDays:function(b,e){if(e>12||e<1)return-1;var f=e-1;return 1==f?b%4==0&&b%100!=0||b%400==0?29:28:calendar.solarMonth[f]},toGanZhiYear:function(b){var e=(b-3)%10,f=(b-3)%12;return 0==e&&(e=10),0==f&&(f=12),calendar.Gan[e-1]+calendar.Zhi[f-1]},toAstro:function(b,e){return"魔羯水瓶双鱼白羊金牛双子巨蟹狮子处女天秤天蝎射手魔羯".substr(2*b-(e<[20,19,21,21,21,22,23,23,23,23,22,22][b-1]?2:0),2)+"座"},toGanZhi:function(b){return calendar.Gan[b%10]+calendar.Zhi[b%12]},getTerm:function(b,e){if(b<1900||b>2100)return-1;if(e<1||e>24)return-1;var f=calendar.sTermInfo[b-1900],c=[parseInt("0x"+f.substr(0,5)).toString(),parseInt("0x"+f.substr(5,5)).toString(),parseInt("0x"+f.substr(10,5)).toString(),parseInt("0x"+f.substr(15,5)).toString(),parseInt("0x"+f.substr(20,5)).toString(),parseInt("0x"+f.substr(25,5)).toString()],a=[c[0].substr(0,1),c[0].substr(1,2),c[0].substr(3,1),c[0].substr(4,2),c[1].substr(0,1),c[1].substr(1,2),c[1].substr(3,1),c[1].substr(4,2),c[2].substr(0,1),c[2].substr(1,2),c[2].substr(3,1),c[2].substr(4,2),c[3].substr(0,1),c[3].substr(1,2),c[3].substr(3,1),c[3].substr(4,2),c[4].substr(0,1),c[4].substr(1,2),c[4].substr(3,1),c[4].substr(4,2),c[5].substr(0,1),c[5].substr(1,2),c[5].substr(3,1),c[5].substr(4,2)];return parseInt(a[e-1])},toChinaMonth:function(b){if(b>12||b<1)return-1;var e=calendar.nStr3[b-1];return e+="月"},toChinaDay:function(b){var e;switch(b){case 10:e="初十";break;case 20:e="二十";break;case 30:e="三十";break;default:e=calendar.nStr2[Math.floor(b/10)],e+=calendar.nStr1[b%10]}return e},getAnimal:function(b){return calendar.Animals[(b-4)%12]},solar2lunar:function(b,e,f){if(b<1900||b>2100)return-1;if(1900==b&&1==e&&f<31)return-1;if(b)var c=new Date(b,parseInt(e)-1,f);else var c=new Date;var a,t=0,r=0,b=c.getFullYear(),e=c.getMonth()+1,f=c.getDate(),d=(Date.UTC(c.getFullYear(),c.getMonth(),c.getDate())-Date.UTC(1900,0,31))/864e5;for(a=1900;a<2101&&d>0;a++)r=calendar.lYearDays(a),d-=r;d<0&&(d+=r,a--);var n=new Date,s=!1;n.getFullYear()==b&&n.getMonth()+1==e&&n.getDate()==f&&(s=!0);var l=c.getDay(),o=calendar.nStr1[l];0==l&&(l=7);var i=a,t=calendar.leapMonth(a),u=!1;for(a=1;a<13&&d>0;a++)t>0&&a==t+1&&0==u?(--a,u=!0,r=calendar.leapDays(i)):r=calendar.monthDays(i,a),1==u&&a==t+1&&(u=!1),d-=r;0==d&&t>0&&a==t+1&&(u?u=!1:(u=!0,--a)),d<0&&(d+=r,--a);var p=a,h=d+1,D=e-1,v=calendar.toGanZhiYear(i),g=calendar.getTerm(i,2*e-1),y=calendar.getTerm(i,2*e),m=calendar.toGanZhi(12*(b-1900)+e+11);f>=g&&(m=calendar.toGanZhi(12*(b-1900)+e+12));var k=!1,C=null;g==f&&(k=!0,C=calendar.solarTerm[2*e-2]),y==f&&(k=!0,C=calendar.solarTerm[2*e-1]);var M=Date.UTC(b,D,1,0,0,0,0)/864e5+25567+10,T=calendar.toGanZhi(M+f-1),I=calendar.toAstro(e,f);return{lYear:i,lMonth:p,lDay:h,Animal:calendar.getAnimal(i),IMonthCn:(u?"闰":"")+calendar.toChinaMonth(p),IDayCn:calendar.toChinaDay(h),cYear:b,cMonth:e,cDay:f,gzYear:v,gzMonth:m,gzDay:T,isToday:s,isLeap:u,nWeek:l,ncWeek:"星期"+o,isTerm:k,Term:C,astro:I}},lunar2solar:function(b,e,f,c){var c=!!c,a=calendar.leapMonth(b);calendar.leapDays(b);if(c&&a!=e)return-1;if(2100==b&&12==e&&f>1||1900==b&&1==e&&f<31)return-1;var t=calendar.monthDays(b,e),r=t;if(c&&(r=calendar.leapDays(b,e)),b<1900||b>2100||f>r)return-1;for(var d=0,n=1900;n<b;n++)d+=calendar.lYearDays(n);for(var s=0,l=!1,n=1;n<e;n++)s=calendar.leapMonth(b),l||s<=n&&s>0&&(d+=calendar.leapDays(b),l=!0),d+=calendar.monthDays(b,n);c&&(d+=t);var o=Date.UTC(1900,1,30,0,0,0),i=new Date(864e5*(d+f-31)+o),u=i.getUTCFullYear(),p=i.getUTCMonth()+1,h=i.getUTCDate();return calendar.solar2lunar(u,p,h)}};