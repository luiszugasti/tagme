# TAGME-LUIS V0.1 - HOW TO

## Folder Structure

Forked from the TAGME Entity Linking Software repo.

In order to run the experiments, please place the following files in the
following folders:

1. Place your qrels (gold standard results) in Tests/Baseline
2. Place your baseline results .test file (the file you obtain after running
   your search and getting your results) into Tests/Baseline
3. Within the Tests directory, create a new directory with the name of the run
   you wish to execute. Next, place a file named testRunCentralityValues.txt.
   This file will contain data similar to the sample one provided.
4. Within the Tests directory, in the directory made from step 3, place a file
   named testRunLambdaValues.txt. This file has the same format as that in step
   3.
5. Place your complete document corpus in the Corpus folder.

This will be enough for you to run experiments with TAGME entity linking and
document graphs.

## Compiling

Todo...

## Running

There are a few settings to ensure from the previous TAGME config files.
If you're me, you can get away with running the same format as that specified
in config.luis.xml (once I provide it). Otherwise, you'll have to follow
the instructions from the original TAGME repo to build all indexes for TAGME
to run correctly.

The next set of settings are those for performing experiments. Actually, it's a
single one: the max amount of top entities to save for document.
The default is 5; when running the code, you can provide option
```--max```. This is a positive integer to use.

* ```--max``` = 0 : use all entities for each document represented (NOT
recommended!)
* ```--max``` > 0 : will store this defined max amount of top entities (as far as
the document allows)

Keep in mind that using a higher value for ```--max``` will likely result
in longer times to complete your experiments (as there are more comparisons to
be made when creating edges). However, you may find there's a sweet spot to
this variable.
