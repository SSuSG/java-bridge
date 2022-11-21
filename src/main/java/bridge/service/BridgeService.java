package bridge.service;

import bridge.domain.BridgeGame;
import bridge.domain.BridgeMaker;
import bridge.domain.GameRetry;

import java.util.*;
public class BridgeService {

    static final BridgeRandomNumberGenerator bridgeRandomNumberGenerator = new BridgeRandomNumberGenerator();
    static final BridgeGame bridgeGame = new BridgeGame(0,0);
    static final BridgeMaker bridgeMaker = new BridgeMaker(bridgeRandomNumberGenerator);
    static final InputValidation inputValidation = new InputValidation();
    static final InputView inputView = new InputView();
    static final OutputView outputView = new OutputView();
    
    public void bridgeGameStart(){
        outputView.printGameStartMessage();
        inputBridgeLength();
        makeBridge();
        TryPlayerMove();
    }

    public void inputBridgeLength(){
        while(true){
            outputView.printRequestBridgeLengthMessage();
            String bridgeLength = inputView.readBridgeSize();
            if(!isValidBridgeLengthInput(bridgeLength))
                continue;

            bridgeGame.setBridgeLength(Integer.parseInt(bridgeLength));
            return;
        }
    }

    public boolean isValidBridgeLengthInput(String bridgeLength){
        if(!inputValidation.isNumber(bridgeLength)){
            outputView.printNotNumberBridgeLengthInputErrorMessage();
            return false;
        }
        if(!inputValidation.isValidRange(Integer.parseInt(bridgeLength))){
            outputView.printInValidRangeBridgeLengthErrorMessage();
            return false;
        }
        return true;
    }

    public void makeBridge(){
        List<String> bridge = bridgeMaker.makeBridge(bridgeGame.getBridgeLength());
        bridgeGame.initializeBridgeGame(bridge);
    }

    public void TryPlayerMove(){
        boolean doRetry = true;
        while(bridgeGame.getBridgeIdx() < bridgeGame.getBridgeSize()){
            if(!inputPlayerMoveDirection())
                continue;
            outputView.printMap(bridgeGame);
            if(!isDoRetry(doRetry))
                return;
        }
        bridgeGame.gameSuccess();
        outputView.printResult(bridgeGame);
    }

    private boolean isDoRetry(boolean doRetry) {
        if(!bridgeGame.isMoveSuccess())
            doRetry = bridgeGame.retry(inputGameRetryCommand());
        if(!doRetry)
            outputView.printResult(bridgeGame);

        return doRetry;
    }

    public boolean inputPlayerMoveDirection(){
        outputView.printRequestMoveDirectionMessage();
        String moveDirection = inputView.readMoving();

        if(!inputValidation.isValidDirection(moveDirection)) {
            outputView.printInValidMoveDirectionErrorMessage();
            return false;
        }
        bridgeGame.move(moveDirection,bridgeGame.getBridgeIdx());
        return true;
    }

    public String inputGameRetryCommand(){
        while(true){
            outputView.printRetryGameMessage();
            String gameRetryCommand = inputView.readGameCommand();
            if(!inputValidation.isValidGameRetryInput(gameRetryCommand)) {
                outputView.printRetryGameErrorMessage();
                continue;
            }
            return gameRetryCommand;
        }
    }



}
