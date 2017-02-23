package com.jspark.android.customview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    CustomView view;

    FrameLayout ground;
    Button btnUp, btnDown, btnLeft, btnRight;

    DisplayMetrics metrics;

    Bitmap block;

    private static final int GROUND_SIZE = 10;

    static int player_x = 0, player_y=0;
    int player_radius=0;
    int unit = 0;

    int flag = 0;
    static int stage = 1;

    static int[][] map = {
            {2,2,2,3,3,3,2,2,2,2},
            {2,2,2,3,4,3,2,2,2,2},
            {2,2,2,3,0,3,3,3,3,3},
            {3,3,3,3,1,0,1,0,4,3},
            {3,4,0,0,1,0,3,3,3,3},
            {3,3,3,3,3,1,3,2,2,2},
            {2,2,2,2,3,0,3,2,2,2},
            {2,2,2,2,3,4,3,2,2,2},
            {2,2,2,2,3,3,3,2,2,2},
            {2,2,2,2,2,2,2,2,2,2}};
    int mapOriginal[][] = {
            {2,2,2,3,3,3,2,2,2,2},
            {2,2,2,3,4,3,2,2,2,2},
            {2,2,2,3,0,3,3,3,3,3},
            {3,3,3,3,1,0,1,0,4,3},
            {3,4,0,0,1,0,3,3,3,3},
            {3,3,3,3,3,1,3,2,2,2},
            {2,2,2,2,3,0,3,2,2,2},
            {2,2,2,2,3,4,3,2,2,2},
            {2,2,2,2,3,3,3,2,2,2},
            {2,2,2,2,2,2,2,2,2,2}};

    int map2[][] = {
            {2,2,2,2,2,2,2,2,2,2},
            {2,2,2,2,2,2,2,2,2,2},
            {2,3,3,3,3,3,3,2,2,2},
            {2,3,0,0,0,0,3,2,2,2},
            {2,3,0,1,1,1,3,3,2,2},
            {2,3,0,0,3,4,4,3,3,3},
            {2,3,0,0,0,4,4,1,0,3},
            {2,3,0,0,0,0,0,0,0,3},
            {2,3,3,3,3,3,3,3,3,3},
            {2,2,2,2,2,2,2,2,2,2}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        block = BitmapFactory.decodeResource(getResources(), R.drawable.block);

        if(stage==1) {
            init(5, 4);
        } else if(stage==2) {
            init(4, 7);
        }

        ground = (FrameLayout)findViewById(R.id.ground);
        btnUp = (Button)findViewById(R.id.btnUp);
        btnDown = (Button)findViewById(R.id.btnDown);
        btnLeft = (Button)findViewById(R.id.btnLeft);
        btnRight = (Button)findViewById(R.id.btnRight);

        btnUp.setOnClickListener(listener);
        btnDown.setOnClickListener(listener);
        btnLeft.setOnClickListener(listener);
        btnRight.setOnClickListener(listener);

        view = new CustomView(this);
        ground.addView(view);
        setCount();
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnUp:
                    if(player_y>0) {
                        if(collisionCheck("up")) {
                            if(pushCheck("up")) {
                                player_y -= 1;
                            } else {

                            }
                        } else {
                            player_y -= 1;
                        }
                    }
                    break;
                case R.id.btnDown:
                    if(player_y<GROUND_SIZE-1) {
                        if(collisionCheck("down")) {
                            if(pushCheck("down")) {
                                player_y += 1;
                            } else {

                            }
                        } else {
                            player_y += 1;
                        }

                    }
                    break;
                case R.id.btnLeft:
                    if(player_x>0) {
                        if(collisionCheck("left")) {
                            if(pushCheck("left")) {
                                player_x -= 1;
                            } else {

                            }
                        } else {
                            player_x -= 1;
                        }
                    }
                    break;
                case R.id.btnRight:
                    if(player_x<GROUND_SIZE-1) {
                        if(collisionCheck("right")) {
                            if(pushCheck("right")) {
                                player_x += 1;
                            } else {

                            }
                        } else {
                            player_x += 1;
                        }

                    }
                    break;
            }
            view.invalidate();
            setCount();
        }
    };

    private void init(int x, int y) {
        metrics = getResources().getDisplayMetrics();
        unit = metrics.widthPixels / GROUND_SIZE;
        player_radius = unit/2;
        player_x = x;
        player_y = y;
    }

    private void setCount() {
        int count = 0;
        for(int i=0;i<map.length;i++) {
            for (int j = 0; j < map[0].length; j++) {
                if(map[i][j]==4) {
                    count++;
                }
            }
        }
        flag = count;
        if(flag==0) {
            ending();
        }
    }

    private boolean collisionCheck(String direction) {
        if(direction.equals("up")) {
            if(map[player_y-1][player_x]==0) {
                return false;
            } else if(map[player_y-1][player_x]==1) {
                return true;
            } else if(map[player_y-1][player_x]==2) {
                return true;
            } else if(map[player_y-1][player_x]==3) {
                return true;
            } else if(map[player_y-1][player_x]==4) {
                Log.w("error", "error");
                return false;
            } else if(map[player_y-1][player_x]==5) {
                return true;
            }
        } else if(direction.equals("down")) {
            if(map[player_y+1][player_x]==0) {
                return false;
            } else if(map[player_y+1][player_x]==1) {
                return true;
            } else if(map[player_y+1][player_x]==2) {
                return true;
            } else if(map[player_y+1][player_x]==3) {
                return true;
            } else if(map[player_y+1][player_x]==4) {
                return false;
            } else if(map[player_y+1][player_x]==5) {
                return true;
            }
        } else if(direction.equals("left")) {
            if (map[player_y][player_x - 1]==0) {
                return false;
            } else if (map[player_y][player_x - 1]==1) {
                return true;
            } else if (map[player_y][player_x - 1]==2) {
                return true;
            } else if (map[player_y][player_x - 1]==3) {
                return true;
            } else if(map[player_y][player_x - 1]==4) {
                return false;
            } else if(map[player_y][player_x - 1]==5) {
                return true;
            }
        } else if(direction.equals("right")) {
            if (map[player_y][player_x + 1]==0) {
                return false;
            } else if (map[player_y][player_x + 1]==1) {
                return true;
            } else if (map[player_y][player_x + 1]==2) {
                return true;
            } else if (map[player_y][player_x + 1]==3) {
                return true;
            } else if(map[player_y][player_x + 1]==4) {
                return false;
            } else if(map[player_y][player_x + 1]==5) {
                return true;
            }
        }
        return false;
    }

    private boolean pushCheck(String direction) {
        if(direction.equals("up")) {
            if(player_y-1>0) {
                if(map[player_y-2][player_x]==0) {
                    if(map[player_y-1][player_x]!=3) {
                        map[player_y - 1][player_x] = 0;
                        map[player_y - 2][player_x] = 1;
                        return true;
                    }
                } else if(map[player_y-2][player_x]==4) {
                    if(map[player_y-1][player_x]==1) {
                        map[player_y - 1][player_x] = 0;
                        map[player_y - 2][player_x] = 5;
                        return true;
                    } else if(map[player_y-1][player_x]==5) {
                        map[player_y - 1][player_x] = 4;
                        map[player_y - 2][player_x] = 5;
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } else if(direction.equals("down")) {
            if(player_y+2<GROUND_SIZE) {
                if(map[player_y+2][player_x]==0) {
                    if(map[player_y+1][player_x]!=3) {
                        map[player_y + 1][player_x] = 0;
                        map[player_y + 2][player_x] = 1;
                        return true;
                    }
                } else if(map[player_y+2][player_x]==4) {
                    if(map[player_y+1][player_x]==1) {
                        map[player_y + 1][player_x] = 0;
                        map[player_y + 2][player_x] = 5;
                        return true;
                    } else if(map[player_y+1][player_x]==5) {
                        map[player_y + 1][player_x] = 4;
                        map[player_y + 2][player_x] = 5;
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } else if(direction.equals("left")) {
            if(player_x-1>0) {
                if(map[player_y][player_x-2]==0) {
                    if(map[player_y][player_x-1]!=3) {
                        map[player_y][player_x - 1] = 0;
                        map[player_y][player_x - 2] = 1;
                        return true;
                    }
                } else if(map[player_y][player_x-2]==4) {
                    if(map[player_y][player_x-1]==1) {
                        map[player_y][player_x - 1] = 0;
                        map[player_y][player_x - 2] = 5;
                        return true;
                    } else if(map[player_y][player_x-1]==5) {
                        map[player_y][player_x - 1] = 4;
                        map[player_y][player_x - 2] = 5;
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } else if(direction.equals("right")) {
            if(player_x+2<GROUND_SIZE) {
                if(map[player_y][player_x + 2]==0) {
                    if(map[player_y][player_x+1]!=3) {
                        map[player_y][player_x + 1] = 0;
                        map[player_y][player_x + 2] = 1;
                        return true;
                    }
                } else if(map[player_y][player_x + 2]==4) {
                    if(map[player_y][player_x+1]==1) {
                        map[player_y][player_x + 1] = 0;
                        map[player_y][player_x + 2] = 5;
                        return true;
                    } else if(map[player_y][player_x + 1]==5) {
                        map[player_y][player_x + 1] = 4;
                        map[player_y][player_x + 2] = 5;
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        view.invalidate();
        return false;
    }

    class CustomView extends View {

        public CustomView(Context context) {
            super(context);
        }

        public CustomView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            Paint paint = new Paint();

            for(int i=0;i<map.length;i++) {
                for(int j=0;j<map[0].length;j++) {
                    if(map[i][j]==1) {
                        paint.setColor(Color.parseColor("#FF2478FF"));
                        canvas.drawRect(unit*j, unit*i, unit*(j+1), unit*(i+1), paint);
                    }
                    if(map[i][j]==2) {
                        paint.setColor(Color.BLACK);
                        canvas.drawRect(unit*j, unit*i, unit*(j+1), unit*(i+1), paint);
                    }
                    if(map[i][j]==3) {
                        Rect r = new Rect(unit*j, unit*i, unit*(j+1), unit*(i+1));
                        canvas.drawBitmap(block, null, r, null);
                    }
                    if(map[i][j]==4) {
                        paint.setColor(Color.parseColor("#FFFFE400"));
                        canvas.drawRect(unit*j, unit*i, unit*(j+1), unit*(i+1), paint);
                    }
                    if(map[i][j]==5) {
                        paint.setColor(Color.parseColor("#FFB95AFF"));
                        canvas.drawRect(unit*j, unit*i, unit*(j+1), unit*(i+1), paint);
                    }
                }
            }

            paint.setColor(Color.BLUE);
            canvas.drawCircle(player_x*unit+player_radius, player_y*unit+player_radius, player_radius, paint);
        }
    }

    public void ending() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Ending");
        ad.setMessage("Retry?");
        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                map = mapOriginal;
                finish();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        }).setNeutralButton("Next Level", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                map = map2;
                stage=2;
                finish();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        }).show();
    }
}