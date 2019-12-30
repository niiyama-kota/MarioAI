#マリオAI
##MainTask3.javaの実行
src/ch/idsia/scenariosの中にあるこのファイルは、ルールベースで自作したOwnAgent2を呼び出してゲームをプレイさせる。
OwnAgent2はsrc/ch/idsia/agents/controllersに配置されている。
##MainTask4-1.java及びMainTask4-2.javaの実行
src/ch/idsia/scenariosの中にあるこのファイルを実行すると、遺伝アルゴリズムを用いて学習させたエージェントがゲームをプレイする。
遺伝アルゴリズムのフレームワークはsrc/ch/idsia/agents/LearningWithGA.javaにあり、src/ch/idsia/scenarios/champ/LearningTrack.javaで学習させる面を指定して実行することで実際に学習させて新しいエージェントを作ることができる。(スコアが最高値を更新した時にsrcと同じディレクトリ内にxmlファイルが作成され、設定した面でfinal Agent agent = (GAAgent)(Easy.load("*.xml"));とすればよい。)
どんな情報を受け取って学習するかを変えたければ、src/ch/agents/GAAgent.javaに格納されている変数inputをいじれば良い。