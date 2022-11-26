<template>
  <div class="search">
    <div class="search_nav">找人/群</div>
    <div class="search_sea">
      <input type="text" class="search_text" placeholder="默认的话">
      <button>搜索</button>
    </div>
  </div>
</template>
<script>
export default {
  name: "ChatSearch",
  setup() {
    
    
  }
}
</script>
<style>
.search {
  width: 24%;
  height: 36%;
  background-color: rgba(50%, 50%, 50%, .4);
  position: fixed;
  bottom: 1vh;
  right: 1vw;
  border: 1px solid white;
}
.search_nav {
  height: 10%;
  border-bottom: 1px solid white;
  text-align: center;
}
.search_sea {
  height: 20%;
  border-bottom: 1px solid white;
}
.search_text {
  margin-top:1.5vh;
  margin-left: 2vh;
}
</style>