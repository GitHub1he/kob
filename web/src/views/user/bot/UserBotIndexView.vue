<template>
  <div class="container content-field" style="margin-top: 15px;">
    <div class="row">
      <div class="col-3">
        <div class="card">
          <div class="card-body">
            <img :src="$store.state.user.photo" alt="" style="width: 100%;">
          </div>
        </div>
      </div>
      <div class="col-9">
        <div class="card">
          <div class="card-body">
            <div class="card-header">
              <span style="font-size: 130%">我的Bot</span> 
              <button type="button" class="btn btn-primary float-end" data-bs-toggle="modal" data-bs-target="#add-bot-btn">
                +bot
              </button>
              <!-- ADD Modal -->
              <div class="modal fade" id="add-bot-btn" tabindex="-1">
                <div class="modal-dialog modal-xl">
                  <div class="modal-content">
                    <div class="modal-header">
                      <h5 class="modal-title">创建bot</h5>
                      <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                      <div class="mb-3">
                        <label for="add-bot-title" class="form-label">名称</label>
                        <input v-model="newbot.title" type="text" class="form-control" id="add-bot-title" placeholder="请输入bot名称">
                      </div>
                      <div class="mb-3">
                        <label for="add-bot-description" class="form-label">简介</label>
                        <textarea v-model="newbot.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入bot简介"></textarea>
                      </div>
                      <div class="mb-3">
                        <label for="add-bot-code" class="form-label">代码</label>
                        <VAceEditor
                          v-model:value="newbot.content"
                          @init="editorInit"
                          lang="c_cpp"
                          theme="textmate"
                          style="height: 300px" />
                      </div>
                    </div>
                    <div class="modal-footer">
                      <div class="error_message">{{ newbot.error_message }}</div>
                      <button type="button" class="btn btn-primary" @click="add_bot">提交</button>
                      <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                    </div>
                  </div>
                </div>
              </div>

            </div>
            <table class="table table-striped table-hover" style="margin-top: 15px;">
              <thead>
                <tr>
                  <th>名称</th>
                  <th>简介</th>
                  <th>修改时间</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="bot in bots" :key="bot.id">
                  <td> {{ bot.title }}</td>
                  <td> {{ bot.description }}</td>
                  <td> {{ bot.modifytime }}</td>
                  <td>
                    <button type="button" class="btn btn-secondary" style="margin-right: 10px" @click="upd_bot(bot)"  data-bs-toggle="modal" :data-bs-target="'#upd-bot-modal-' + bot.id">修改</button>
                    <button type="button" class="btn btn-danger" @click="del_bot(bot)">删除</button>

                    <!--UPDATE Modal -->
                    <div class="modal fade" :id="'upd-bot-modal-' + bot.id " tabindex="-1">
                      <div class="modal-dialog modal-xl">
                        <div class="modal-content">
                          <div class="modal-header">
                            <h5 class="modal-title">修改bot</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                          </div>
                          <div class="modal-body">
                            <div class="mb-3">
                              <label for="add-bot-title" class="form-label">名称</label>
                              <input v-model="bot.title" type="text" class="form-control" id="add-bot-title" placeholder="请输入bot名称">
                            </div>
                            <div class="mb-3">
                              <label for="add-bot-description" class="form-label">简介</label>
                              <textarea v-model="bot.description" class="form-control" id="add-bot-description" rows="3" placeholder="请输入bot简介"></textarea>
                            </div>
                            <div class="mb-3">
                              <label for="add-bot-code" class="form-label">代码</label>
                              <VAceEditor
                                v-model:value="bot.content"
                                @init="editorInit"
                                lang="c_cpp"
                                theme="textmate"
                                style="height: 300px" />
                            </div>
                          </div>
                          <div class="modal-footer">
                            <div class="error_message">{{ newbot.error_message }}</div>
                            <button type="button" class="btn btn-primary" @click="upd_bot(bot)">保存修改</button>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">我在想想~~</button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref,reactive } from 'vue'
import $ from 'jquery'
import { useStore } from 'vuex'
import { Modal } from 'bootstrap/dist/js/bootstrap'
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';

export default{
  components: {
    VAceEditor,
  },
  setup() {
    
    ace.config.set(
      "basePath", 
      "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")
    const store = useStore();
    let bots = ref([]);

    const newbot = reactive({
      title: "",
      description: "",
      content: "",
      error_message: "",
    });

    const refresh_bots = () => {
      $.ajax({
        url: "http://127.0.0.1:3000/user/bot/getlist/",
        type: "GET",
        headers:{
          'Authorization': "Bearer " + store.state.user.token,
        }, 
        success(resp) {
          bots.value = resp;
        }
      })
    }

    const add_bot = () => {
      newbot.error_message = "";
      $.ajax({
        url: "http://127.0.0.1:3000/user/bot/add/",
        type: "POST",
        data: {
          title: newbot.title,
          description: newbot.description,
          content: newbot.content,
        },
        headers:{
          'Authorization': "Bearer " + store.state.user.token,
        }, 
        success(resp) {
          if(resp.error_message === "success") {
            
            newbot.title = "";
            newbot.description = "";
            newbot.content = "";
            Modal.getInstance("#add-bot-btn").hide();
            refresh_bots();
          }else {
            newbot.error_message = resp.error_message;
          }
        }
      })
    }
    
    const upd_bot = (bot) => {
      newbot.error_message = "";
      $.ajax({
        url: "http://127.0.0.1:3000/user/bot/update/",
        type: "POST",
        data: {
          bot_id: bot.id ,
          title: bot.title,
          description: bot.description,
          content: bot.content,
        },
        headers:{
          'Authorization': "Bearer " + store.state.user.token,
        }, 
        success(resp) {
          if(resp.error_message === "success") {
            newbot.title = "";
            newbot.description = "";
            newbot.content = "";
            Modal.getInstance('#upd-bot-modal-' + bot.id).hide();
            refresh_bots();
          }else {
            newbot.error_message = resp.error_message;
          }
        }
      })
    }

    const del_bot = (bot) => {
      newbot.error_message = "";
      $.ajax({
        url: "http://127.0.0.1:3000/user/bot/del/",
        type: "DELETE",
        data: {
          bot_id: bot.id,
        },
        headers:{
          'Authorization': "Bearer " + store.state.user.token,
        }, 
        success(resp) {
          if(resp.error_message === "success") {
            refresh_bots();
          }else {
            newbot.error_message = resp.error_message;
          }
        }
      })
    }

    refresh_bots();
    return {
      bots,
      newbot,
      add_bot,
      upd_bot,
      del_bot,
    }
  }
}
</script>

<style scoped>
.error_message{
  color: red;
}
</style>