<template>
  <div style="min-height:100px">
     <turning-steps-bar v-if="inIDE" :list="[{status:1},{status:2},{status:3}]" :itemWidth="itemWidth" :value="value" #default="current" @cellClick="hanldeClick" > 
      <div class="turning-steps-body" vusion-slot-name="default">
          <slot :item="current.item" :index="index"></slot>
      </div>
    </turning-steps-bar>
    <turning-steps-bar v-if="!inIDE" :list="dataSource" :itemWidth="itemWidth" :value="value" #default="current" @cellClick="hanldeClick" > 
      <div class="turning-steps-body" >
        <slot :item="current.item">
        </slot>
      </div>
    </turning-steps-bar>
  </div>
</template>

<script>
import { TurningStepsBar } from "../../lib/index.js"
export default {
    name:"lxy-turning-step-bar",
    props:{
      value:{
        type:String,
      },
      dataSource:{
        type:Array,
      },
      itemWidth:{
        type:String,
        default:"20%"
      }
    },
    components:{
      TurningStepsBar
    },
    computed: {
      inIDE() {
        return (this.$env && this.$env.VUE_APP_DESIGNER) || false;
      },
    },
    methods:{
      hanldeClick(e){
        console.log(e);
        this.$emit("onCellClick",e)
      }
    }
}
</script>

<style>

</style>