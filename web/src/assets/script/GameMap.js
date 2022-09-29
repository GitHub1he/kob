import { AcGameObject } from "./AcGameObject";
import { Wall } from "./Wall";

export class GameMap extends AcGameObject {
  constructor(ctx,parent) {
    super();

    this.ctx = ctx;
    this.parent = parent;
    this.L = 0; //每一个格子的长度

    this.rows = 13;
    this.cols = 13;

    this.inner_walls_count = 18;
    this.walls = [];
  }

  check_connectivity(g, sx, sy, tx, ty) {
    if(sx == tx && sy == ty) return true;
    g[sx][sy] = true;

    let dx = [-1 ,0, 1, 0], dy = [1, 1, 0, -1];
    for(let i = 0; i < 4 ;i++){
      let x = sx + dx[i],y = sy + dy[i];
      if(!g[x][y] && this.check_connectivity(g,x,y,tx,ty))
        return true;
    }
    return false;
  }

  create_walls(){
    const g = [];
    //给四周加上障碍物
    for(let r = 0 ; r < this.rows; r++){
      g[r] = [];
      for(let c = 0; c < this.cols ; c ++){
        if(r === 0 || c === 0 || r+1 === this.rows || c+1 === this.cols){
          g[r][c] = true;
        }else {
          g[r][c] = false;
        }
      }
    }
    //创建随机障碍物
    for(let i = 0 ; i < this.inner_walls_count; i++){
      for(let j = 0 ; j < 1000 ; j++){
        let r = parseInt(Math.random() * this.rows);
        let c = parseInt(Math.random() * this.cols);
        //保证不是出生点和已经是墙的值不被覆盖
        if (g[r][c] || g[c][r] || r == this.rows -2 && c ==1 || c == this.cols -2 && r == 1) continue;

        g[r][c] = g[c][r] = true;
        break;
      }
    }

    const copy_g = JSON.parse(JSON.stringify(g));
    if(!this.check_connectivity(copy_g,this.rows - 2 , 1 , 1 , this.cols - 2))
     return false;

    for(let r = 0 ; r < this.rows; r++){
      for(let c = 0; c < this.cols ; c ++){
        if(g[r][c]) {
          this.walls.push(new Wall(r,c,this));
        }
      }
    }

    return true;
  }

  start() {
    for(let i = 0; i < 100; i++){
      if(this.create_walls()) break;
    }
  }

  update_size(){
    this.L = parseInt(Math.min(this.parent.clientWidth/this.cols ,this.parent.clientHeight/this.rows));
    this.ctx.canvas.width = this.L * this.cols;
    this.ctx.canvas.height = this.L * this.rows;
  }

  update() {
    this.update_size();
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