/*
 * Copyright (c) 2009-2010, Sergey Karakovskiy and Julian Togelius
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Mario AI nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.GeneralizerLevelScene;
import ch.idsia.benchmark.mario.engine.LevelScene;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.engine.sprites.Sprite;
import ch.idsia.benchmark.mario.environments.Environment;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Apr 8, 2009
 * Time: 4:03:46 AM
 */

public class OwnAgent2 extends BasicMarioAIAgent implements Agent
{
	int trueJumpCounter = 0;
	int trueSpeedCounter = 0;
	private final LevelScene levelScene;

	public OwnAgent2()
	{
		super("OwnAgent");
		this.levelScene = new LevelScene();
		reset();
	}

	public void reset()
	{
		action = new boolean[Environment.numberOfKeys];
		action[Mario.KEY_RIGHT]= true;
	}


	float y1=0,y2;
	float x1=0,x2;
	float x_e1 = 255, x_e2;
	public boolean[] getAction()
	{
		x2 = marioFloatPos[0];
		y2 = marioFloatPos[1];
		if(enemiesFloatPos.length>0) {
			x_e2 = enemiesFloatPos[1];
		}

		//jump
		action[Mario.KEY_JUMP] = false;
		if(isObstacle(marioEgoRow,marioEgoCol+1)) {
			action[Mario.KEY_JUMP] = (!isMarioOnGround || isMarioAbleToJump);
		}
		if(isEnemy(0,2)) {
			action[Mario.KEY_JUMP] = false;
		}
		if(isEnemy(1,3) || isEnemy(2,3)) {
			action[Mario.KEY_JUMP] = (!isMarioOnGround || isMarioAbleToJump);
		}
		if(isHole(1)) {
			action[Mario.KEY_JUMP] = (!isMarioOnGround || isMarioAbleToJump);
		}

		//dash
		action[Mario.KEY_SPEED] = false;
		if(isEnemy(1,3) || isEnemy(2,3) || isEnemy(-1,3) || isEnemy(-2,3) || isEnemy(1,-3) || isEnemy(-1,-3) || isEnemy(2,-3) || isEnemy(-2,-3)) {
			if((isEnemy(1,2) || isEnemy(-1,2) || isEnemy(2,2) || isEnemy(-2,2)) && (marioMode==2)) {
				action[Mario.KEY_SPEED] = true;
				trueSpeedCounter++;
			}
			else {
				action[Mario.KEY_SPEED] = false;
				trueSpeedCounter = 0;
			}
		}
		else {
			if(trueSpeedCounter == 0 && (marioMode==2)) {
				action[Mario.KEY_SPEED] = isMarioAbleToShoot;
				trueSpeedCounter++;
			}
			else {
				action[Mario.KEY_SPEED] = false;
				trueSpeedCounter = 0;
			}
		}
		if((isHole(0) || isHole(1))) {
			action[Mario.KEY_SPEED] = true;
			trueSpeedCounter++;
		}

		//left
		action[Mario.KEY_LEFT] = false;
		if(isEnemy(0,3) || isEnemy(1,4) || isEnemy(2,4)) {
			action[Mario.KEY_LEFT] = true;
		}
		if((getReceptiveFieldCellValue(marioEgoRow,marioEgoCol+1)==-85 && getEnemiesCellValue(marioEgoRow,marioEgoCol+1)==91)
				|| (getReceptiveFieldCellValue(marioEgoRow,marioEgoCol+2)==-85 && getEnemiesCellValue(marioEgoRow,marioEgoCol+2)==91)) {
			action[Mario.KEY_LEFT] = false;
		}
		if((falling(y1,y2) && isHole(1))) {
			action[Mario.KEY_LEFT] = true;
		}

		//right
		action[Mario.KEY_RIGHT] = true;
		if(!isMarioOnGround && isEnemy(0,-2)) {
			action[Mario.KEY_RIGHT] = false;
		}
		if(isEnemy(0,3) || isEnemy(1,3) || isEnemy(2,3)) {
			action[Mario.KEY_RIGHT] = false;
		}
		if((getEnemiesCellValue(marioEgoRow,marioEgoCol+1)==91 || getEnemiesCellValue(marioEgoRow,marioEgoCol+2)==91)
				|| (getReceptiveFieldCellValue(marioEgoRow,marioEgoCol+2)==-85 && getEnemiesCellValue(marioEgoRow,marioEgoCol+2)==91)) {
			action[Mario.KEY_RIGHT] = true;
		}
		if(falling(y1,y2) && isHole(1) && isHole(2)) {
			action[Mario.KEY_RIGHT] = false;
		}

		x1 = x2;
		y1 = y2;
		return action;
	}

	public boolean isObstacle(int r, int c){
		return getReceptiveFieldCellValue(r,
				c)==GeneralizerLevelScene.BRICK
				|| getReceptiveFieldCellValue(r,
						c)==GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH
						|| getReceptiveFieldCellValue(r,
								c)==GeneralizerLevelScene.FLOWER_POT_OR_CANNON
								|| getReceptiveFieldCellValue(r,
										c)==GeneralizerLevelScene.LADDER;
	}

	public boolean noObstacle(int r, int c) {
		return getReceptiveFieldCellValue(r,c) == 0;
	}

	public boolean isHole(int fr) {
		boolean ins = noObstacle(marioEgoRow,marioEgoCol+fr);
		for(int i=1;i<10;i++) {
			ins = ins && noObstacle(marioEgoRow+i,marioEgoCol+fr);
		}
		return ins;
	}

	public boolean isEnemy(int x, int y) {
		boolean ins = false;
		for(int i=0;i<=y;i++) {
			ins = ins || ((getEnemiesCellValue(marioEgoRow-i,marioEgoCol+x)!=Sprite.KIND_NONE && getEnemiesCellValue(marioEgoRow-i,marioEgoCol+x)!= 25));
		}
		return ins;
	}


	public boolean falling(float y1, float y2) {
		return (y2-y1 >= 0);
	}

}