export default function timetable(state = [], action){
    switch(action.type){
        case 'ADD':{
            state = action.payload;
            break;
        }
        default: break;
    }
    return state;
}
