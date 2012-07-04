function varargout = exercise3(varargin)
% EXERCISE3 M-file for exercise3.fig
%      EXERCISE3, by itself, creates a new EXERCISE3 or raises the existing
%      singleton*.
%
%      H = EXERCISE3 returns the handle to a new EXERCISE3 or the handle to
%      the existing singleton*.
%
%      EXERCISE3('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in EXERCISE3.M with the given input arguments.
%
%      EXERCISE3('Property','Value',...) creates a new EXERCISE3 or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before exercise3_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to exercise3_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help exercise3

% Last Modified by GUIDE v2.5 13-Apr-2011 14:36:21

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
    'gui_Singleton',  gui_Singleton, ...
    'gui_OpeningFcn', @exercise3_OpeningFcn, ...
    'gui_OutputFcn',  @exercise3_OutputFcn, ...
    'gui_LayoutFcn',  [] , ...
    'gui_Callback',   []);
if nargin && ischar(varargin{1})
    gui_State.gui_Callback = str2func(varargin{1});
end

if nargout
    [varargout{1:nargout}] = gui_mainfcn(gui_State, varargin{:});
else
    gui_mainfcn(gui_State, varargin{:});
end
% End initialization code - DO NOT EDIT


% --- Executes just before exercise3 is made visible.
function exercise3_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to exercise3 (see VARARGIN)

% Choose default command line output for exercise3
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);

% UIWAIT makes exercise3 wait for user response (see UIRESUME)
% uiwait(handles.figure1);
set(handles.text4, 'String', num2str(10));

% --- Outputs from this function are returned to the command line.
function varargout = exercise3_OutputFcn(hObject, eventdata, handles)
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;


% --- Executes on button press in pushbutton1.
function pushbutton1_Callback(hObject, eventdata, handles)
% hObject    handle to pushbutton1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

left_money_h = findobj(gcbf, 'Tag', 'text4');
left_money = str2double(get(left_money_h, 'String'));

bet_h = findobj(gcbf, 'Tag', 'edit1');
bet = str2double(get(bet_h, 'String'));
if(isnan(bet))
    return;
end
if((bet > left_money) | (bet < 0) )
    uiwait(msgbox(strcat('You bet', num2str(bet), ' Euro.' )));
    return;
end

bet_number_h = findobj(gcbf, 'Tag', 'edit2');
bet_number = str2double(get(bet_number_h, 'String'));
if(isnan(bet))
    return;
end
if(~(bet_number <=6 & bet_number >= 0))
    warning('The bet number should be within 0-6 range.');
    return;
end
textptr = findobj(gcbf, 'Tag', 'text1');

y = ceil(6*rand);
switch y
    case 1
        set(textptr, 'string', 1)
    case 2
        set(textptr, 'string', 2)
    case 3
        set(textptr, 'string', 3)
    case 4
        set(textptr, 'string', 4)
    case 5
        set(textptr, 'string', 5)
    case 6
        set(textptr, 'string', 6)
end

if(y == bet_number)
    left_money = left_money + bet;
else
    left_money = left_money - bet;
    end
set(left_money_h, 'String', num2str(left_money));
if( left_money <= 0 )
    uiwait(msgbox('You run out of money.' ));
    delete(gcbf);
     return;
end
button = questdlg('Continue or Quit?', 'Question', 'Continue', 'Quit','Continue');
switch button
    case 'Continue'
        return;
    case 'Quit'
        delete(gcbf);
end


function edit1_Callback(hObject, eventdata, handles)
% hObject    handle to edit1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of edit1 as text
%        str2double(get(hObject,'String')) returns contents of edit1 as a double


% --- Executes during object creation, after setting all properties.
function edit1_CreateFcn(hObject, eventdata, handles)
% hObject    handle to edit1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function edit2_Callback(hObject, eventdata, handles)
% hObject    handle to edit2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of edit2 as text
%        str2double(get(hObject,'String')) returns contents of edit2 as a double


% --- Executes during object creation, after setting all properties.
function edit2_CreateFcn(hObject, eventdata, handles)
% hObject    handle to edit2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end