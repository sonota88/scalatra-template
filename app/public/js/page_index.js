class View {
  static render(state){
    return TreeBuilder.build(h=>
      h("div", {}, "TODO")
    );
  }
}

class Page {
  constructor(){
    this.state = {};
  }

	init(){
    console.log("init");
    this.render();
  }

  getTitle(){
    return "scalatra-template index";
  }

  render(){
    $("#main").empty().append(View.render(this.state));
  }
}
	
__g.ready(new Page());
