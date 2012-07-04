clear
left_money = 10;

while(1)
    bet = inputdlg(strcat('You have ', num2str(left_money) ,' Euro.  How much would you like to bet?'));
    if(isempty(bet))
        break;
    end
    bet = str2double(bet);
    if(isnan(bet))
        continue;
    end
    if((bet > left_money) | (bet < 0) )
        warning('You bet %d Euro.', bet);
        continue;
    end

    bet_number = inputdlg('Which number you prefer?');
    if(isempty(bet_number))
        break;
    end
    bet_number = str2double(bet_number);
    if(isnan(bet_number))
        continue;
    end
    if(~(bet_number <=6 & bet_number >= 0))
        warning('The bet number should be within 0-6 range.');
        continue;
    end


    roll = ceil(6*rand);
    uiwait(msgbox(strcat('The dice rolled ', num2str(roll))));
    if( bet_number == roll )
        left_money = left_money + bet;
    else
        left_money = left_money - bet;
    end

    uiwait(msgbox(strcat('You have left', num2str(left_money)),'Money','modal'));

    if( left_money <= 0 )
        break;
    end

    button = questdlg('Continue or Quit?', 'Question', 'Continue', 'Quit','Continue');
    switch button
        case 'Continue'
            continue;
        case 'Quit'
            break;
    end
end
msgbox('Game Over.');