import React, {Component} from 'react';

import Head from './part/head'
import HomeCarousel from './part/homeCarousel'

class Main extends Component {
    render() {
        const carouselProps = {
            pictures: [
                {
                    src: "https://gusilebedi.spb.ru/capitoliy/wp-content/uploads/sites/2/2017/04/18.jpg",
                    title: "Welcome to our restaurant chain",
                },
                {
                    src: "https://p1.zoon.ru/preview/-U8f9I8hoXhQ8rYe8j885A/1920x1080x75/1/6/8/original_58d012a740c088df378b53f7_5937b223560d9.jpg",
                    title: "Russian Kitchen",
                },
                {
                    src: "https://assets.resto.ru/data/spb/places/105250/c5de.jpg",
                    title: "Italian Kitchen"
                },
                {
                    src: "http://хаус-тв.рф/wp-content/uploads/2016/03/20160326_144433.jpg",
                    title: "Japanese Kitchen"
                }
            ],
        };
        return(
            <div className="main-page">
                <Head/>
                <HomeCarousel {...carouselProps}/>
            </div>
        )
    }
}

export default(Main);
