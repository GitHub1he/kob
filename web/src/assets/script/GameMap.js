import { AcGameObject } from "./AcGameObject";
import { Snake } from "./Snake";
import { Wall } from "./Wall";

export class GameMap extends AcGameObject {
  constructor(ctx,parent,store) {
    super();

    this.ctx = ctx;
    this.parent = parent;
    this.store = store;
    this.L = 0; //每一个格子的长度

    this.rows = 13;
    this.cols = 14;

    this.inner_walls_count = 18;
    this.walls = [];

    this.snakes = [
      new Snake({id: 0, color: "#466f95", r: this.rows -2,c: 1}, this),
      new Snake({id: 1, color: "#deaeb1", r: 1 ,c: this.cols -2}, this),
    ];

  }

  
  start() {
    this.create_walls();  

    this.add_listening_events()
  }

  add_listening_events (){
    if (this.store.state.record.is_record) {
      let k = 0;
      const [snake0, snake1] = this.snakes;
      const a_steps = this.store.state.record.a_steps;
      const b_steps = this.store.state.record.b_steps;
      const winer = this.store.state.record.record_winer;
      const interval_id = setInterval(() => {
        if(k >= a_steps.length -1) {
          if(winer === "all" || winer === "A"){
            snake1.status = "die";
          }
          if(winer === "all" || winer === "B"){
            snake0.status = "die";
          }
          clearInterval(interval_id);
        } else {
          snake0.set_direction(parseInt(a_steps[k]));
          snake1.set_direction(parseInt(b_steps[k]));
        }
        k++;
      }, 300)
    } else {
      this.ctx.canvas.focus();

      this.ctx.canvas.addEventListener("keydown",e => {
        let d = -1;
        if (e.key === 'w' || e.key === 'ArrowUp') d = 0;
        else if(e.key === 'd' || e.key === 'ArrowRight') d = 1;
        else if(e.key === 's' || e.key === 'ArrowDown') d = 2;
        else if(e.key === 'a' || e.key === 'ArrowLeft') d = 3;

        if(d >= 0) {
          this.store.state.pk.socket.send(JSON.stringify({
            event: "move", 
            direction: d,
          }))
        }
      });
    }
  }

  check_valid(cell) { //判断目标位置是否合法：没有撞到墙或者两条蛇的身体
    for(const wall of this.walls) {
      if(wall.r === cell.r && wall.c === cell.c)
        return false;
    }

    for (const snake of this.snakes) {
      let k = snake.cells.length;
      if(!snake.check_tail_increasing()){ //蛇尾部不变长时最后一格可用
        k --;
      }
      for(let i = 0 ; i < k ; i++){
        if(snake.cells[i].r === cell.r && snake.cells[i].c === cell.c)
          return false;
      }
    }

    return true;
  }

  check_ready() { //判断两条蛇是否都准备号下一回合
    for(const snake of this.snakes){
      if(snake.status !== "idle") return false;
      if(snake.direction === -1) return false;
    }
    return true;
  }

  create_walls(){
    const g = this.store.state.pk.gamemap;
    for(let r = 0 ; r < this.rows; r++){
      for(let c = 0; c < this.cols ; c ++){
        if(g[r][c]) {
          this.walls.push(new Wall(r,c,this));
        }
      }
    }
  }

  update_size(){
    this.L = parseInt(Math.min(this.parent.clientWidth/this.cols ,this.parent.clientHeight/this.rows));
    this.ctx.canvas.width = this.L * this.cols;
    this.ctx.canvas.height = this.L * this.rows;
  }
  
  next_step() { //让两条蛇进入下一回合
    for(const snake of this.snakes){
      snake.next_step();
    }
  }

  update() {
    this.update_size();
    if(this.check_ready()) {
      this.next_step();
    }
    this.render();
  }

  render() {
    const color_even = "#AAD751", color_odd = "#7eceab";
    for(let r = 0; r < this.rows; r++){
      for(let c = 0; c < this.cols;c++){
        if((r + c) % 2 == 0){
          this.ctx.fillStyle = color_even;
        }else {
          this.ctx.fillStyle = color_odd;
        }
        this.ctx.fillRect(c * this.L, r * this.L,this.L,this.L);
      }
    }
  }
}