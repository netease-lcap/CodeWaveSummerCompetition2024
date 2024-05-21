<template>
  <!--  <div>{{value}}</div>-->
  <div class="sidebar">
    <div class="icon-sidebar">
      <img src="" style="width: 30px;height: 30px">
      <!--      <ul id="it">-->
      <!--      </ul>-->
      <div id="item" >
        <icon-sidebar-item v-for="(tempitem,index) in items" :key="index" :text="tempitem.text" :url="tempitem.url" :value="index"></icon-sidebar-item>
      </div>
    </div>
  </div>
</template>
<script>

import IconSidebarItem from "../icon-sidebar-item";

export default {
  name: "icon-sidebar",
  components: {IconSidebarItem},
  props: {
    primaryColor: {
      type: String,
      default: "#FF4500"
    },
    iconUrl: {
      type: String,
      default: "https://img.zcool.cn/community/01e4315542ab990000019ae99f4ef0.jpg@1280w_1l_2o_100sh.jpg"
    }
  },
  data() {
    return {
      items: [
        {
          text: '侧边栏1',
          url: '#'
        },
        {
          text: '侧边栏2',
          url: '#'
        },
        {
          text: '侧边栏3',
          url: '#'
        }]
    };
  },
  mounted() {
    this.run();
  },

  watch: {
    primaryColor() {
      if (this.primaryColor.length > 0) {
        this.run();
      }
    },
    iconUrl() {
      if (this.iconUrl.length > 0) {
        this.run();
      }
    }
  },
  methods: {
    async loadImg() {
      var image = new Image();
      image.crossOrigin = 'anonymous';
      image.src = this.iconUrl;
      return new Promise(resolve => {
        image.onload = function () {
          resolve(image)
        }
      })
    },
    async run() {
      // console.log("加载完毕");
      var nav = document.getElementsByClassName('nav')[0];
      var item = document.getElementById("item");
      var ulWidth;
      var ulHeight;
      let maxWidth;
      var fold = true;
      for (let i = 0; i < item.childNodes.length; i++) {
        // let item = document.createElement("item");
        // item.innerText = "菜单项" + (i + 1);
        // item.value=i;
        console.log(item.childNodes[i].childNodes[0].href)
        item.childNodes[i].addEventListener("mouseover", () => {
          // item.style.backgroundColor = "#FF4500";
          if (item.childNodes[i].style.color !== "rgb(255, 255, 255)")
            item.childNodes[i].style.color = this.primaryColor;
        });
        item.childNodes[i].addEventListener("mouseout", () => {
          //console.log(item.style.color);
          if (item.childNodes[i].style.color !== "rgb(255, 255, 255)") {
            item.childNodes[i].style.color = "#000000"
          }
        })
        // item.addEventListener('click',()=>{
        //   item.style.backgroundColor=this.primaryColor;
        //   item.style.color="#ffffff"
        // })
        maxWidth = maxWidth > item.childNodes[i].offsetWidth ? maxWidth : item.childNodes[i].offsetWidth;
        // item.append(item);
      }
      for (let i = 0; i < item.childNodes.length; i++) {
        // let item = document.createElement("item");
        // item.innerText = "菜单项" + (i + 1);
        // item.value=i;
        item.childNodes[i].addEventListener("mouseover", () => {
          // item.style.backgroundColor = "#FF4500";
          if (item.childNodes[i].style.color !== "rgb(255, 255, 255)")
            item.childNodes[i].style.color = this.primaryColor;
        });
        item.childNodes[i].addEventListener("mouseout", () => {
          //console.log(item.style.color);
          if (item.childNodes[i].style.color !== "rgb(255, 255, 255)") {
            item.childNodes[i].style.color = "#000000"
          }
        })
        // item.addEventListener('click',()=>{
        //   item.style.backgroundColor=this.primaryColor;
        //   item.style.color="#ffffff"
        // })
        maxWidth = maxWidth > item.childNodes[i].offsetWidth ? maxWidth : item.childNodes[i].offsetWidth;
        // item.append(item);
      }
      //console.log(item.childNodes[0]);
      // 为每个侧边栏加上点击效果
      for (let i = 0; i < item.childNodes.length; i++) {
        item.childNodes[i].addEventListener('click', () => {
          for (let j = 0; j < item.childNodes.length; j++) {
            item.childNodes[j].style.backgroundColor = "#ffffff";
            item.childNodes[j].style.color = "#000000"
          }
          item.childNodes[i].style.backgroundColor = this.primaryColor;
          item.childNodes[i].style.color = "#ffffff";
        })
      }
      item.style.width = maxWidth + "px";
      if (this.iconUrl) {
        let img = document.getElementsByTagName('img')[0];
        img.style.width = "30px";
        img.style.height = "30px";
        img.src = this.iconUrl;
        img.style.visibility = 'visible';
        let sidebar = document.getElementById("sidebar");
        img.addEventListener("click", () => {
          if (fold === true) {
            item.style.visibility = "hidden";
            ulWidth = item.offsetWidth;
            ulHeight = item.offsetHeight;
            img.style.width = "30px";
            img.style.height = "30px";
            item.style.width = "0px";
            item.style.height = "0px";
            fold = false;
          } else if (fold === false) {
            item.style.visibility = "visible";
            item.style.width = ulWidth + "px";
            item.style.height = ulHeight + "px";
            ulWidth = "0px";
            ulHeight = "0px";
            fold = true;
          }
          // img.addEventListener("click",()=>{
          //   item.style.visibility="visible";
          //   item.style.width=ulWidth+"px";
          //   item.style.height=ulHeight+"px";
          //   ulWidth="0px";
          //   ulHeight="0px";
          //   // if(document.getElementsByTagName("img")[0])
          //   //   sidebar.removeChild(document.getElementsByTagName("img")[0]);
          // })
          //sidebar.append(img);
        });
      }
    },
  }
}
</script>

<style>
.icon-sidebar {
}

.icon-sidebar img {
  visibility: hidden;
  border-radius: 50%;
}

.sidebar li {
  list-style-type: none;
}
</style>