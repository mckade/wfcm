# Group Name: 
  ProcMusic
  
# Group Members:
McKade Umbenhower, Taylor Bleizeffer, Robert Randolph

## Description of the project
  This project uses Markov Chains and ideas from [Wave Function Collapse](https://github.com/mxgmn/WaveFunctionCollapse) to procedurally generate music based on a user-input sample. The project uses MIDI files to input/output music.
  
  Upon recieving an input sample, we construct [Markov Chain Transition Matrices](https://en.wikipedia.org/wiki/Stochastic_matrix) that holds probabilistic constraints related to possible pitch and note duration transitions. One such possible input may look like the following.
  
  ![alt text](https://github.com/cosc495x/sd-project-procmusic/blob/master/res/Presentation/input.PNG)
  
  After constructing these Markov Tables, we feed them to the Wave Function Collapse algorithm. WaveFC holds superpositions for all possible notes and note durations. For each iteration of the algorithm, we collapse one superposition (and therefore define a note feature, i.e., pitch, duration). All surrounding superpositions to this feature definition are then analyzed by checking the associated Markov Table and updated so no impossibilities remain. This collapse/propogate step is repeated until all features for all notes are defined, leaving the resulting output piece. The following GIF depicts this process.
  
  ![Alt Text](https://github.com/cosc495x/sd-project-procmusic/blob/master/docs/wfc.gif)
  
  Running this algorithm on the previous example input sample can result in some of the following outputs.
  
  ![alt_text](https://github.com/cosc495x/sd-project-procmusic/blob/master/res/Presentation/out1.PNG)
  
  ![alt_text](https://github.com/cosc495x/sd-project-procmusic/blob/master/res/Presentation/out2.PNG)
