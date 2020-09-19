package de.stylextv.snake.game;

import java.util.ArrayList;

import de.stylextv.snake.math.MathUtil;

public class CycleManager {
	
	private static int[][] path=new int[64][44];
	
	static {
		byte[][] states = new byte[32][22];
		ArrayList<Integer> frontiers=new ArrayList<Integer>();
		int startX=MathUtil.getRandom().nextInt(32);
		int startY=MathUtil.getRandom().nextInt(22);
		setBit(states, startX, startY, 0, true);
		addFrontiers(startX,startY,frontiers,states);
		while(frontiers.size()!=0) {
			int index=frontiers.remove(MathUtil.getRandom().nextInt(frontiers.size()));
			int x=index%32;
			int y=index/32;
			setBit(states, x, y, 0, true);
			int dir=MathUtil.getRandom().nextInt(4);
			for(int i=0; i<4; i++) {
				if(dir==0) {
					if(markParent(x, y, x+1, y, dir, states)) break;
				} else if(dir==1) {
					if(markParent(x, y, x, y+1, dir, states)) break;
				} else if(dir==2) {
					if(markParent(x, y, x-1, y, dir, states)) break;
				} else {
					if(markParent(x, y, x, y-1, dir, states)) break;
				}
				dir=(dir+1)%4;
			}
			addFrontiers(x, y, frontiers,states);
		}
		
		int pathX=0;
		int pathY=0;
		int dir=0;
		int index=0;
		while(true) {
			path[pathX][pathY]=index;
			dir=dir-1;
			if(dir<0) dir+=4;
			while(true) {
				if(dir==0) {
					int gotoX=pathX+1;
					if(gotoX<64) {
						if((gotoX/2==pathX/2)||getBit(states, pathX/2, pathY/2, dir+1)) {
							pathX=gotoX;
							break;
						}
					}
				} else if(dir==1) {
					int gotoY=pathY+1;
					if(gotoY<44) {
						if((gotoY/2==pathY/2)||getBit(states, pathX/2, pathY/2, dir+1)) {
							pathY=gotoY;
							break;
						}
					}
				} else if(dir==2) {
					int gotoX=pathX-1;
					if(gotoX>=0) {
						if((gotoX/2==pathX/2)||getBit(states, pathX/2, pathY/2, dir+1)) {
							pathX=gotoX;
							break;
						}
					}
				} else {
					int gotoY=pathY-1;
					if(gotoY>=0) {
						if((gotoY/2==pathY/2)||getBit(states, pathX/2, pathY/2, dir+1)) {
							pathY=gotoY;
							break;
						}
					}
				}
				dir=(dir+1)%4;
			}
			
			if(pathX==0&&pathY==0) break;
			index++;
		}
	}
	private static boolean markParent(int x, int y, int parentX, int parentY, int dir, byte[][] states) {
		if(parentX>=0&&parentY>=0 && parentX<32&&parentY<22) {
			if(getBit(states, parentX, parentY, 0)) {
				setBit(states, x, y, dir+1, true);
				int opposite=(dir+2)%4;
				setBit(states, parentX, parentY, opposite+1, true);
				return true;
			}
		}
		return false;
	}
	private static void addFrontiers(int x, int y, ArrayList<Integer> frontiers, byte[][] states) {
		if(x>0) addFrontier(x-1, y, frontiers, states);
		if(x<31) addFrontier(x+1, y, frontiers, states);
		if(y>0) addFrontier(x, y-1, frontiers, states);
		if(y<21) addFrontier(x, y+1, frontiers, states);
	}
	private static void addFrontier(int x, int y, ArrayList<Integer> frontiers, byte[][] states) {
		if(getBit(states, x, y, 0)) return;
		int i=y*32+x;
		for(int j:frontiers) {
			if(i==j) return;
		}
		frontiers.add(i);
	}
	private static void setBit(byte[][] states, int x, int y, int pos, boolean b) {
		byte byt=states[x][y];
		if(b) {
			byt |= 1 << pos;
		} else {
			byt &= ~(1 << pos);
		}
		states[x][y]=byt;
	}
	private static boolean getBit(byte[][] states, int x, int y, int pos) {
		int mask = 1<<pos;
		return (states[x][y] & mask) != 0;
	}
	
	public static int getIndex(int x, int y) {
		return path[x][y];
	}
	
}
