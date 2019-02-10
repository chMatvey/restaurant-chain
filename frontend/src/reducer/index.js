import {combineReducers} from 'redux';
import {routerReducer} from 'react-router-redux';
import chef from './chef';
import timetable from './timetable';

export default combineReducers({
    routing: routerReducer,
    chef,
    timetable
})
